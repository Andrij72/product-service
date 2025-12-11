package com.akul.microservices.product.service;

import com.akul.microservices.product.dto.ProductRequest;
import com.akul.microservices.product.dto.ProductResponse;
import com.akul.microservices.product.exception.ProductAlreadyExistsException;
import com.akul.microservices.product.exception.ProductNotFoundException;
import com.akul.microservices.product.model.Product;
import com.akul.microservices.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ProductService.java.
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        if (productRepository.existsBySku(productRequest.sku())) {
            throw new ProductAlreadyExistsException(productRequest.sku());
        }
        Product product = Product.builder()
                .sku(productRequest.sku())
                .name(productRequest.name())
                .price(productRequest.price())
                .description(productRequest.description())
                .build();

        productRepository.save(product);
        log.info("Successfully created product: {}", product);
        return new ProductResponse(product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Found {} products in DB", products.size());
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }

    public List<ProductResponse> createProducts(List<ProductRequest> products) {
        return products.stream()
                .map(this::createProduct)
                .toList();
    }

    public void deleteBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
        productRepository.deleteBySku(sku);
        log.info("Successfully deleted product: {}", product);
    }

    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        return ProductResponse.from(product);
    }

    public ProductResponse updateProduct(String sku,
                                         ProductRequest productRequest) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        Product updatedProduct = productRepository.save(product);

        return ProductResponse.from(updatedProduct);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
