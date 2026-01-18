package com.akul.microservices.product.exception;

/**
 * ProductAlreadyExistsException.java.
 *
 * @author Andrii Kulynych
 * @since 12/10/2025
 */
public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String sku) {
        super("Product with SKU '%s' already exists".formatted(sku));
    }

}
