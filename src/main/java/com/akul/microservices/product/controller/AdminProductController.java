package com.akul.microservices.product.controller;

import com.akul.microservices.product.dto.AdminProductResponse;
import com.akul.microservices.product.dto.ProductRequest;
import com.akul.microservices.product.dto.ProductUpdateRequest;
import com.akul.microservices.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AdminProductController.java.
 *
 * @author Andrii Kulynych
 * @since 12/31/2025
 */
@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    public Page<AdminProductResponse> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.getAdminProducts(pageable);
    }

    @GetMapping("/{sku}")
    private AdminProductResponse getProductBySku(@PathVariable  String sku) {
        return productService.getAdminProduct(sku);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminProductResponse createProduct(
            @Valid @RequestBody ProductRequest request
    ) {
        return productService.createAdminProduct(request);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<AdminProductResponse> createProducts(
            @RequestBody final List<ProductRequest> products) {
        return products.stream()
                .map(productService::createAdminProduct)
                .toList();
    }

    @PutMapping("/{sku}")
    public AdminProductResponse updateProduct(
            @PathVariable String sku,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return productService.updateAdminProduct(sku, request);
    }

    @PatchMapping("/{sku}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableProduct(@PathVariable String sku) {
        productService.disableProduct(sku);
    }

    @PatchMapping("/{sku}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableProduct(@PathVariable String sku) {
        productService.enableProduct(sku);
    }

    @DeleteMapping("/batch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducts(@RequestBody List<String> skus) {
        productService.deleteProducts(skus);
    }
}
