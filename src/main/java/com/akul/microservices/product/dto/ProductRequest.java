package com.akul.microservices.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * ProductRequest.java.
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */
public record ProductRequest(
        @NotBlank(message = "SKU must not be blank")
        String sku,

        @NotBlank(message = "Name must not be blank")
        String name,

        String description,

        @NotNull(message = "Price must not be null")
        @Positive(message = "Price must be positive")
        BigDecimal price) {
}
