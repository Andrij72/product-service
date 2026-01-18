package com.akul.microservices.product.controller;

import com.akul.microservices.product.dto.ProductResponse;
import com.akul.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductController.java.
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductResponse> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Sort sort = Sort.by(
                Sort.Order.asc("sku"),
                Sort.Order.asc("name"),
                Sort.Order.asc("price"),
                Sort.Order.asc("createdAt")
        );

        return productService.getPublicProducts(
                PageRequest.of(page, size, sort));
    }

    @GetMapping("/{sku}")
    public ProductResponse getProduct(@PathVariable String sku) {
        return productService.getPublicProductBySku(sku);
    }

}
