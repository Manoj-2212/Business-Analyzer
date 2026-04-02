package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/truncate")
    public String truncateDb() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        jdbcTemplate.execute("TRUNCATE TABLE sale_items;");
        jdbcTemplate.execute("TRUNCATE TABLE sales;");
        jdbcTemplate.execute("TRUNCATE TABLE alerts;");
        jdbcTemplate.execute("TRUNCATE TABLE products;");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
        return "Database truncated successfully!";
    }
}
