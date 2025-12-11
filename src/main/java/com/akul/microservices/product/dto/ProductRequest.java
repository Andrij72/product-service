package com.akul.microservices.product.dto;

import java.math.BigDecimal;

/**
 * ProductRequest.java.
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */
public record ProductRequest(
        String sku,
        String name,
        String description,
        BigDecimal price) {
}
