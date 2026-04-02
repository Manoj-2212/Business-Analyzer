package com.example.demo.service;

import com.example.demo.model.Alert;
import com.example.demo.model.Product;
import com.example.demo.repository.AlertRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Alert> getAllAlerts() {
        generateAlerts();
        return alertRepository
            .findAllByOrderByCreatedAtDesc();
    }

    public List<Alert> getUnreadAlerts() {
        return alertRepository
            .findByIsReadFalseOrderByCreatedAtDesc();
    }

    public long getUnreadCount() {
        return alertRepository.countByIsReadFalse();
    }

    public Alert markAsRead(Long id) {
        Alert alert = alertRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Alert not found: " + id));
        alert.setIsRead(true);
        return alertRepository.save(alert);
    }

    public void markAllAsRead() {
        List<Alert> unread = alertRepository
            .findByIsReadFalseOrderByCreatedAtDesc();
        for (Alert alert : unread) {
            alert.setIsRead(true);
            alertRepository.save(alert);
        }
    }

    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }

    private void generateAlerts() {
        List<Product> lowStockProducts =
            productRepository.findLowStockProducts();
        List<Alert> existingUnreadAlerts = getUnreadAlerts();

        for (Product product : lowStockProducts) {
            boolean exists = alertRepository.existsByTypeAndProductNameAndIsReadFalse(Alert.AlertType.LOW_STOCK, product.getName());
            if (exists) continue;
            String message = "Low stock warning: "
                + product.getName()
                + " has only "
                + product.getStockQuantity()
                + " units left!";

            Alert alert = new Alert();
            alert.setType(Alert.AlertType.LOW_STOCK);
            alert.setMessage(message);
            alert.setProductName(product.getName());
            alert.setIsRead(false);

            if (product.getStockQuantity() == 0) {
                alert.setSeverity(
                    Alert.AlertSeverity.CRITICAL);
            } else if (product.getStockQuantity()
                    <= 5) {
                alert.setSeverity(
                    Alert.AlertSeverity.HIGH);
            } else {
                alert.setSeverity(
                    Alert.AlertSeverity.MEDIUM);
            }

            alertRepository.save(alert);
        }

        List<Product> deadStockProducts =
            productRepository.findDeadStockProducts();

        for (Product product : deadStockProducts) {
            boolean exists = alertRepository.existsByTypeAndProductNameAndIsReadFalse(Alert.AlertType.DEAD_STOCK, product.getName());
            if (exists) continue;
            String message = "Dead stock alert: "
                + product.getName()
                + " has 0 units in stock!";

            Alert alert = new Alert();
            alert.setType(Alert.AlertType.DEAD_STOCK);
            alert.setMessage(message);
            alert.setProductName(product.getName());
            alert.setIsRead(false);
            alert.setSeverity(Alert.AlertSeverity.CRITICAL);

            alertRepository.save(alert);
        }
    }
}
