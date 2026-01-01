package com.akul.microservices.product.dto;

import com.akul.microservices.product.model.Product;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * AdminProductResponse.java.
 *
 * @author Andrii Kulynych
 * @since 12/31/2025
 */
public record AdminProductResponse(
        String id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        boolean enabled,
        Instant createdAt
) {
    public static AdminProductResponse from(Product product) {
        return new AdminProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isEnabled(),
                product.getCreatedAt()
        );
    }
}
