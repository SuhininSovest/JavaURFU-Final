package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-db")
    public String testDatabase() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            System.out.println("Успешное подключение к базе данных!");
            return "Подключение к базе данных успешно!";
        } catch (Exception e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
            return "Ошибка подключения к базе данных: " + e.getMessage();
        }
    }
} 