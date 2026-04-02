package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardStats {

    private BigDecimal monthlyRevenue;
    private BigDecimal monthlyProfit;
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private Long todaySales;
    private Long totalSales;
    private Long totalProducts;
    private Long lowStockCount;
    private List<Map<String, Object>> bestSellers;
    private List<Map<String, Object>> monthlySalesChart;
    private List<Map<String, Object>> monthlyProfitChart;

    public DashboardStats() {}

    public BigDecimal getMonthlyRevenue() { return monthlyRevenue; }
    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public BigDecimal getMonthlyProfit() { return monthlyProfit; }
    public void setMonthlyProfit(BigDecimal monthlyProfit) {
        this.monthlyProfit = monthlyProfit;
    }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalProfit() { return totalProfit; }
    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Long getTodaySales() { return todaySales; }
    public void setTodaySales(Long todaySales) {
        this.todaySales = todaySales;
    }

    public Long getTotalSales() { return totalSales; }
    public void setTotalSales(Long totalSales) {
        this.totalSales = totalSales;
    }

    public Long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(Long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public List<Map<String, Object>> getBestSellers() {
        return bestSellers;
    }
    public void setBestSellers(List<Map<String, Object>> bestSellers) {
        this.bestSellers = bestSellers;
    }

    public List<Map<String, Object>> getMonthlySalesChart() {
        return monthlySalesChart;
    }
    public void setMonthlySalesChart(
            List<Map<String, Object>> monthlySalesChart) {
        this.monthlySalesChart = monthlySalesChart;
    }

    public List<Map<String, Object>> getMonthlyProfitChart() {
        return monthlyProfitChart;
    }
    public void setMonthlyProfitChart(
            List<Map<String, Object>> monthlyProfitChart) {
        this.monthlyProfitChart = monthlyProfitChart;
    }
}
