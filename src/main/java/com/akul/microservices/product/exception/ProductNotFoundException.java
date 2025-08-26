package com.akul.microservices.product.exeption;

/**
 * ProductNotFoundException.java
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/21/2025
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String name) {
        super("Product with name '%s' is absent!".formatted(name));
    }
}
