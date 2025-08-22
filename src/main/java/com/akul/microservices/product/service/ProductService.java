package com.akul.microservices.product.service;

import com.akul.microservices.product.dto.ProductRequest;
import com.akul.microservices.product.dto.ProductResponse;
import com.akul.microservices.product.model.Product;
import com.akul.microservices.product.repository.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductService.java
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

        Product product = Product.builder()
                .name(productRequest.name())
                .price(productRequest.price())
                .description(productRequest.description())
                .build();
        productRepository.save(product);
        log.info("Successfully created product: {}", product);
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }


    public ProductResponse getProduct(String name) {
        return productRepository.findByName(name)
                .map(ProductResponse::from)
                .orElseThrow(
                        () -> new RuntimeException("Product with name %s is absent!".formatted(name))
                );
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
}
