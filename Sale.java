package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number",
        unique = true, nullable = false)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @OneToMany(mappedBy = "sale",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<SaleItem> items;

    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent =
        BigDecimal.ZERO;

    @Column(name = "tax_percent")
    private BigDecimal taxPercent = BigDecimal.ZERO;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount =
        BigDecimal.ZERO;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    private BigDecimal profit = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod =
        PaymentMethod.CASH;

    @Enumerated(EnumType.STRING)
    private SaleStatus status =
        SaleStatus.COMPLETED;

    private String notes;

    @Column(name = "sale_date")
    private LocalDateTime saleDate =
        LocalDateTime.now();

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public enum PaymentMethod {
        CASH, CARD, UPI, BANK_TRANSFER
    }

    public enum SaleStatus {
        COMPLETED, CANCELLED, REFUNDED
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    public void setInvoiceNumber(
            String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public User getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(
            String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }
    public void setCustomerPhone(
            String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(
            String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public List<SaleItem> getItems() {
        return items;
    }
    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }
    public void setDiscountPercent(
            BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }
    public void setTaxPercent(
            BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    public void setDiscountAmount(
            BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    public void setTaxAmount(
            BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(
            BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getProfit() {
        return profit;
    }
    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(
            PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public SaleStatus getStatus() {
        return status;
    }
    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }
    public void setSaleDate(
            LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}