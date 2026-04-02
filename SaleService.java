package com.example.demo.service;

import com.example.demo.dto.DashboardStats;
import com.example.demo.dto.SaleRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.Sale;
import com.example.demo.model.SaleItem;
import com.example.demo.model.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SaleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Sale createSale(
            SaleRequest request,
            String username) {

        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "User", "username", username
                )
            );

        Sale sale = new Sale();
        sale.setInvoiceNumber("TEMP");
        sale.setCreatedBy(user);
        sale.setCustomerName(
            request.getCustomerName());
        sale.setCustomerPhone(
            request.getCustomerPhone());
        sale.setCustomerEmail(
            request.getCustomerEmail());
        sale.setNotes(request.getNotes());
        
        if (request.getSaleDate() != null) {
            sale.setSaleDate(request.getSaleDate());
        } else {
            sale.setSaleDate(LocalDateTime.now());
        }

        if (request.getDiscountPercent() != null) {
            sale.setDiscountPercent(
                request.getDiscountPercent());
        } else {
            sale.setDiscountPercent(BigDecimal.ZERO);
        }

        if (request.getTaxPercent() != null) {
            sale.setTaxPercent(
                request.getTaxPercent());
        } else {
            sale.setTaxPercent(BigDecimal.ZERO);
        }

        if (request.getPaymentMethod() != null) {
            try {
                sale.setPaymentMethod(
                    Sale.PaymentMethod.valueOf(
                        request.getPaymentMethod()
                    )
                );
            } catch (Exception e) {
                sale.setPaymentMethod(
                    Sale.PaymentMethod.CASH);
            }
        }

        List<SaleItem> saleItems =
            new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;

        for (SaleRequest.SaleItemRequest itemReq
                : request.getItems()) {

            Product product = productRepository
                .findById(itemReq.getProductId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Product", "id",
                        itemReq.getProductId()
                    )
                );

            if (product.getStockQuantity()
                    < itemReq.getQuantity()) {
                throw new BusinessException(
                    "Insufficient stock for: "
                    + product.getName()
                    + ". Available: "
                    + product.getStockQuantity()
                );
            }

            SaleItem saleItem = new SaleItem();
            saleItem.setSale(sale);
            saleItem.setProduct(product);
            saleItem.setQuantity(
                itemReq.getQuantity());
            saleItem.setUnitPrice(
                product.getSellingPrice());
            saleItem.setCostPrice(
                product.getCostPrice());

            BigDecimal lineTotal =
                product.getSellingPrice()
                    .multiply(BigDecimal.valueOf(
                        itemReq.getQuantity()
                    ));
            BigDecimal lineProfit =
                product.getSellingPrice()
                    .subtract(product.getCostPrice())
                    .multiply(BigDecimal.valueOf(
                        itemReq.getQuantity()
                    ));

            saleItem.setLineTotal(lineTotal);
            saleItem.setLineProfit(lineProfit);

            subtotal = subtotal.add(lineTotal);
            totalProfit =
                totalProfit.add(lineProfit);

            product.setStockQuantity(
                product.getStockQuantity()
                - itemReq.getQuantity()
            );
            productRepository.save(product);
            saleItems.add(saleItem);
        }

        sale.setItems(saleItems);
        sale.setSubtotal(subtotal);

        BigDecimal discountAmount = subtotal
            .multiply(sale.getDiscountPercent())
            .divide(
                BigDecimal.valueOf(100),
                2,
                RoundingMode.HALF_UP
            );

        BigDecimal afterDiscount =
            subtotal.subtract(discountAmount);

        BigDecimal taxAmount = afterDiscount
            .multiply(sale.getTaxPercent())
            .divide(
                BigDecimal.valueOf(100),
                2,
                RoundingMode.HALF_UP
            );

        BigDecimal totalAmount =
            afterDiscount.add(taxAmount);

        sale.setDiscountAmount(discountAmount);
        sale.setTaxAmount(taxAmount);
        sale.setTotalAmount(totalAmount);
        sale.setProfit(totalProfit);
        sale.setStatus(Sale.SaleStatus.COMPLETED);
        sale.setInvoiceNumber("INV-" + System.currentTimeMillis());

        return saleRepository.save(sale);
    }

    public Page<Sale> getAllSales(Pageable pageable) {
        return saleRepository.findByIsDeletedFalse(pageable);
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Sale", "id", id
                )
            );
    }

    public DashboardStats getDashboardStats() {
        LocalDateTime startOfMonth =
            LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay =
            LocalDate.now().atStartOfDay();

        DashboardStats stats =
            new DashboardStats();

        try {
            BigDecimal monthlyRevenue =
                saleRepository.getTotalRevenue(
                    startOfMonth, now);
            stats.setMonthlyRevenue(
                monthlyRevenue != null
                    ? monthlyRevenue
                    : BigDecimal.ZERO
            );
        } catch (Exception e) {
            stats.setMonthlyRevenue(
                BigDecimal.ZERO);
        }

        try {
            BigDecimal monthlyProfit =
                saleRepository.getTotalProfit(
                    startOfMonth, now);
            stats.setMonthlyProfit(
                monthlyProfit != null
                    ? monthlyProfit
                    : BigDecimal.ZERO
            );
        } catch (Exception e) {
            stats.setMonthlyProfit(
                BigDecimal.ZERO);
        }

        try {
            Long todaySales =
                saleRepository.countSales(
                    startOfDay, now);
            stats.setTodaySales(
                todaySales != null
                    ? todaySales : 0L
            );
        } catch (Exception e) {
            stats.setTodaySales(0L);
        }

        stats.setTotalSales(
            saleRepository.count());
        stats.setTotalProducts(
            productRepository.count());

        try {
            BigDecimal totalRevenue =
                saleRepository.getTotalLifetimeRevenue();
            stats.setTotalRevenue(
                totalRevenue != null
                    ? totalRevenue
                    : BigDecimal.ZERO
            );
        } catch (Exception e) {
            stats.setTotalRevenue(BigDecimal.ZERO);
        }

        try {
            BigDecimal totalProfit =
                saleRepository.getTotalLifetimeProfit();
            stats.setTotalProfit(
                totalProfit != null
                    ? totalProfit
                    : BigDecimal.ZERO
            );
        } catch (Exception e) {
            stats.setTotalProfit(BigDecimal.ZERO);
        }

        try {
            stats.setLowStockCount(
                (long) productRepository
                    .findLowStockProducts()
                    .size()
            );
        } catch (Exception e) {
            stats.setLowStockCount(0L);
        }

        try {
            List<Map<String, Object>>
                bestSellers = new ArrayList<>();
            for (Object[] row : saleRepository
                    .findBestSellers()) {
                Map<String, Object> item =
                    new HashMap<>();
                item.put("name", row[0]);
                item.put("quantity", row[1]);
                bestSellers.add(item);
            }
            stats.setBestSellers(bestSellers);
        } catch (Exception e) {
            stats.setBestSellers(
                new ArrayList<>());
        }

        int currentYear =
            LocalDate.now().getYear();

        try {
            List<Map<String, Object>>
                monthlySales = new ArrayList<>();
            for (Object[] row : saleRepository
                    .getMonthlySales(currentYear)) {
                Map<String, Object> item =
                    new HashMap<>();
                item.put("month", row[0]);
                item.put("revenue", row[1]);
                monthlySales.add(item);
            }
            stats.setMonthlySalesChart(
                monthlySales);
        } catch (Exception e) {
            stats.setMonthlySalesChart(
                new ArrayList<>());
        }

        try {
            List<Map<String, Object>>
                monthlyProfit = new ArrayList<>();
            for (Object[] row : saleRepository
                    .getMonthlyProfit(currentYear)) {
                Map<String, Object> item =
                    new HashMap<>();
                item.put("month", row[0]);
                item.put("profit", row[1]);
                monthlyProfit.add(item);
            }
            stats.setMonthlyProfitChart(
                monthlyProfit);
        } catch (Exception e) {
            stats.setMonthlyProfitChart(
                new ArrayList<>());
        }

        return stats;
    }

    public void deleteSale(Long id) {
        Sale sale = getSaleById(id);
        sale.setIsDeleted(true);
        saleRepository.save(sale);
    }
}