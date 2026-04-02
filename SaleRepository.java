package com.example.demo.repository;

import com.example.demo.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    Page<Sale> findByIsDeletedFalse(Pageable pageable);

    List<Sale> findByCustomerNameContainingIgnoreCase(String customerName);
    
    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.isDeleted = false")
    BigDecimal getTotalLifetimeRevenue();

    @Query("SELECT SUM(s.profit) FROM Sale s WHERE s.isDeleted = false")
    BigDecimal getTotalLifetimeProfit();

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end")
    BigDecimal getTotalRevenue(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("SELECT SUM(s.profit) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end")
    BigDecimal getTotalProfit(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end")
    Long countSales(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("SELECT si.product.name, SUM(si.quantity) as qty FROM SaleItem si GROUP BY si.product.name ORDER BY qty DESC")
    List<Object[]> findBestSellers();

    @Query("SELECT MONTH(s.saleDate), SUM(s.totalAmount) FROM Sale s WHERE YEAR(s.saleDate) = :year GROUP BY MONTH(s.saleDate) ORDER BY MONTH(s.saleDate)")
    List<Object[]> getMonthlySales(@Param("year") int year);

    @Query("SELECT MONTH(s.saleDate), SUM(s.profit) FROM Sale s WHERE YEAR(s.saleDate) = :year GROUP BY MONTH(s.saleDate) ORDER BY MONTH(s.saleDate)")
    List<Object[]> getMonthlyProfit(@Param("year") int year);
}