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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    /**
     * Retrieve a paginated list of all products.
     *
     * @param page    the page number (default 0)
     * @param size    the page size (default 10)
     * @param sortBy  the field to sort by (default "createdAt")
     * @param sortDir the sort direction, "asc" or "desc" (default "desc")
     * @return a page of AdminProductResponse objects
     */
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

    /**
     * Retrieve a single product by its SKU.
     *
     * @param sku the unique SKU of the product
     * @return the AdminProductResponse for the product
     */
    @GetMapping("/{sku}")
    public AdminProductResponse getProductBySku(@PathVariable String sku) {
        return productService.getAdminProduct(sku);
    }

    /**
     * Create a new product without an image.
     * Accepts JSON only.
     *
     * @param request the product data in JSON format
     * @return the created AdminProductResponse
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminProductResponse createProduct(
            @Valid @RequestBody ProductRequest request) {
        return productService.createAdminProduct(
                request, null);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<AdminProductResponse> createProducts(
            @RequestBody final List<ProductRequest> products) {
        return products.stream()
                .map(productService::createAdminProduct)
                .toList();
    }

    /**
     * Add or update the image of an existing product.
     * Accepts multipart/form-data with a single file.
     *
     * @param sku  the SKU of the product to update
     * @param file the image file to upload
     * @return the updated AdminProductResponse including the new image info
     */
    @PutMapping("/{sku}/image")
    public AdminProductResponse updateProductImage(
            @PathVariable String sku,
            @RequestPart("file") MultipartFile file
    ) {
        return productService.updateProductImage(sku, file);
    }
    /**
     * Update the basic information of
     * an existing product without changing its image.
     * Accepts JSON only.
     *
     * @param sku     the SKU of the product to update
     * @param request the updated product data in JSON format
     * @return the updated AdminProductResponse
     */
    @PutMapping("/{sku}")
    public AdminProductResponse updateProduct(
            @PathVariable String sku,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return productService.updateAdminProduct(sku, request, null);
    }

    /**
     * Disable a product (mark it as inactive) by its SKU.
     *
     * @param sku the SKU of the product to disable
     */
    @PatchMapping("/{sku}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableProduct(@PathVariable String sku) {
        productService.disableProduct(sku);
    }

    /**
     * Enable a product (mark it as active) by its SKU.
     *
     * @param sku the SKU of the product to enable
     */
    @PatchMapping("/{sku}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableProduct(@PathVariable String sku) {
        productService.enableProduct(sku);
    }

    /**
     * Delete multiple products by their SKUs.
     *
     * @param skus a list of SKUs to delete
     */
    @DeleteMapping("/batch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducts(@RequestBody List<String> skus) {
        productService.deleteProducts(skus);
    }
}
