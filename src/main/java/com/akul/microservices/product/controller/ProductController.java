package com.akul.microservices.product.controller;

import com.akul.microservices.product.dto.ProductRequest;
import com.akul.microservices.product.dto.ProductResponse;
import com.akul.microservices.product.model.Product;
import com.akul.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController.java
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductResponse> createProducts(@RequestBody List<ProductRequest> products) {
        return productService.createProducts(products);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductByName(@RequestParam("name") String name) {
        return productService.getProduct(name)
                ;
    }

}
