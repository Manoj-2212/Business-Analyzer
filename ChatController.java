package com.example.demo.controller;

import com.example.demo.service.SaleService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(
            @RequestBody Map<String, String> request) {

        String message = request.get("message")
            .toLowerCase();
        String response = generateResponse(message);

        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return ResponseEntity.ok(result);
    }

    private String generateResponse(String message) {

        if (message.contains("revenue") ||
                message.contains("sales today")) {
            return "📊 Your monthly revenue is tracked "
                + "in the Dashboard. Check the Analytics "
                + "section for detailed revenue charts!";
        }

        if (message.contains("low stock") ||
                message.contains("stock")) {
            long lowStockCount = productService
                .getLowStockProducts().size();
            return "⚠️ You currently have "
                + lowStockCount
                + " products with low stock. "
                + "Go to Smart Alerts to see details!";
        }

        if (message.contains("profit")) {
            return "💰 Your profit analysis is available "
                + "in the Reports section. "
                + "Check monthly profit trends there!";
        }

        if (message.contains("best seller") ||
                message.contains("top product")) {
            return "🏆 Check the Dashboard for your "
                + "Best Sellers chart showing "
                + "top selling products!";
        }

        if (message.contains("invoice") ||
                message.contains("bill")) {
            return "🧾 Go to Sales & Billing section "
                + "to create new invoices and "
                + "view all past bills!";
        }

        if (message.contains("product") ||
                message.contains("inventory")) {
            long totalProducts = productService
                .getAllProducts().size();
            return "📦 You have "
                + totalProducts
                + " active products in inventory. "
                + "Go to Inventory to manage them!";
        }

        if (message.contains("hello") ||
                message.contains("hi")) {
            return "👋 Hello! I am your Business "
                + "Assistant. Ask me about sales, "
                + "revenue, stock, profits or invoices!";
        }

        if (message.contains("help")) {
            return "🤖 I can help you with:\n"
                + "• Sales & Revenue info\n"
                + "• Stock & Inventory status\n"
                + "• Profit analysis\n"
                + "• Best selling products\n"
                + "• Invoice management\n"
                + "Just ask me anything!";
        }

        return "🤔 I am not sure about that. "
            + "Try asking about sales, revenue, "
            + "stock levels, profits or invoices!";
    }
}
