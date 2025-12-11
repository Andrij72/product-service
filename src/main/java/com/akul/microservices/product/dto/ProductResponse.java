package com.akul.microservices.product.dto;

import com.akul.microservices.product.model.Product;

import java.math.BigDecimal;

/**
 * ProductResponse.java.
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */
public record ProductResponse(String sku,
                              String name,
                              String description,
                              BigDecimal price) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
