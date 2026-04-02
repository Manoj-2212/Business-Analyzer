package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAllProducts(Pageable pageable, String category) {
        if (category != null && !category.isEmpty()) {
            return productRepository.findByCategoryAndActiveTrue(category, pageable);
        }
        return productRepository.findByActiveTrue(pageable);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return getAllProducts(pageable, null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue(Pageable.unpaged()).getContent();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Product", "id", id));
    }

    public Product createProduct(ProductRequest request) {
        if (request.getSku() != null &&
                productRepository.existsBySku(request.getSku())) {
            throw new BusinessException(
                "Product with SKU already exists: "
                + request.getSku());
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setStockQuantity(
            request.getStockQuantity() != null
            ? request.getStockQuantity() : 0);
        product.setLowStockThreshold(
            request.getLowStockThreshold() != null
            ? request.getLowStockThreshold() : 10);
        product.setUnit(request.getUnit());
        product.setSku(request.getSku());
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setLowStockThreshold(request.getLowStockThreshold());
        product.setUnit(request.getUnit());
        product.setSku(request.getSku());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public Product updateStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        if (product.getStockQuantity() + quantity < 0) {
            throw new BusinessException(
                "Insufficient stock for product: "
                + product.getName());
        }
        product.setStockQuantity(
            product.getStockQuantity() + quantity);
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    public List<Product> getDeadStockProducts() {
        return productRepository.findDeadStockProducts();
    }

    public List<Product> searchProducts(String name) {
        return productRepository
            .findByNameContainingIgnoreCase(name);
    }

    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }
}