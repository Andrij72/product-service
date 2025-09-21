package com.akul.microservices.product.controller;

import com.akul.microservices.product.dto.ProductRequest;
import com.akul.microservices.product.dto.ProductResponse;
import com.akul.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductResponse createProduct(
            @RequestBody final ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductResponse> createProducts(
            @RequestBody final List<ProductRequest> products) {
        return productService.createProducts(products);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductByName(
            @RequestParam("name") final String name) {
        return productService.getProduct(name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable final String id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Product successfully deleted!");
    }
}
