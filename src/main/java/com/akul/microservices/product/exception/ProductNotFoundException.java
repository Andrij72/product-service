package com.akul.microservices.product.exception;

/**
 * ProductNotFoundException.java.
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/21/2025
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(final String sku) {
        super("Product with SKU '%s' is absent!".formatted(sku));
    }
}
