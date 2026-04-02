package com.example.demo.repository;

import com.example.demo.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByIsReadFalseOrderByCreatedAtDesc();

    List<Alert> findAllByOrderByCreatedAtDesc();

    long countByIsReadFalse();

    List<Alert> findByType(Alert.AlertType type);

    boolean existsByTypeAndProductNameAndIsReadFalse(Alert.AlertType type, String productName);
}
