package com.example.demo.controller;

import com.example.demo.model.Alert;
import com.example.demo.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:3000")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(
            alertService.getAllAlerts());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Alert>> getUnreadAlerts() {
        return ResponseEntity.ok(
            alertService.getUnreadAlerts());
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        return ResponseEntity.ok(
            alertService.getUnreadCount());
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Alert> markAsRead(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            alertService.markAsRead(id));
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<String> markAllAsRead() {
        alertService.markAllAsRead();
        return ResponseEntity.ok(
            "All alerts marked as read!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlert(
            @PathVariable Long id) {
        alertService.deleteAlert(id);
        return ResponseEntity.ok(
            "Alert deleted successfully!");
    }
}