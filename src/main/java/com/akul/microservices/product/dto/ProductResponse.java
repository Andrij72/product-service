package com.akul.microservices.product.dto;

import com.akul.microservices.product.model.Product;

import java.math.BigDecimal;

/**
 * ProductResponse.java
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */
public record ProductResponse(String id, String name, String description, Double price) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
