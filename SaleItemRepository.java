package com.example.demo.repository;

import com.example.demo.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findBySaleId(Long saleId);

    @Query("SELECT si.product.name, SUM(si.quantity), SUM(si.lineProfit) FROM SaleItem si GROUP BY si.product.name ORDER BY SUM(si.quantity) DESC")
    List<Object[]> getProductSalesSummary();
}