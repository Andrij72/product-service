package com.akul.microservices.product.dto;

/**
 * FileDto.java.
 *
 * @author Andrii Kulynych
 * @since 1/26/2026
 */
public record FileDto(
        String objectName,
        String contentType,
        long size,
        String presignedUrl
) {
}
