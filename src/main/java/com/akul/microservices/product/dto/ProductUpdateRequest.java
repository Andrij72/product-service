package com.akul.microservices.product.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * ProductUpdateRequest.java.
 *
 * @author Andrii Kulynych
 * @since 1/1/2026
 */
public record ProductUpdateRequest(
        @NotBlank String name,
        String description,
        BigDecimal price
) {
}
