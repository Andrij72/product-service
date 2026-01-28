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
        String sku,
        String name,
        String description,
        BigDecimal price,
        boolean enabled,
        Instant createdAt,
        String imageObjectName,
        String previewUrl
) {
    public static AdminProductResponse from(
            Product product, String previewUrl) {
        return new AdminProductResponse(
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isEnabled(),
                product.getCreatedAt(),
                product.getImageObjectName(),
                previewUrl
        );
    }
}
