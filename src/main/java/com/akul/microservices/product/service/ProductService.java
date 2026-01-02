package com.akul.microservices.product.service;

import com.akul.microservices.product.dto.AdminProductResponse;
import com.akul.microservices.product.dto.ProductRequest;
import com.akul.microservices.product.dto.ProductResponse;
import com.akul.microservices.product.dto.ProductUpdateRequest;
import com.akul.microservices.product.exception.ProductAlreadyExistsException;
import com.akul.microservices.product.exception.ProductNotFoundException;
import com.akul.microservices.product.model.Product;
import com.akul.microservices.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    public AdminProductResponse createAdminProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new ProductAlreadyExistsException(request.sku());
        }

        Product product = Product.builder().sku(request.sku())
                .name(request.name()).description(request.description())
                .price(request.price()).enabled(true).build();

        Product saved = productRepository.save(product);
        log.info("Admin created product {}", saved.getSku());

        return AdminProductResponse.from(saved);
    }

    public AdminProductResponse updateAdminProduct(
            String sku, ProductUpdateRequest request) {
        Product product = productRepository.findBySku(sku).orElseThrow(
                () -> new ProductNotFoundException(sku));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());

        Product saved = productRepository.save(product);
        log.info("Admin updated product {}", sku);

        return AdminProductResponse.from(saved);
    }

    public void disableProduct(String sku) {
        Product product = productRepository.findBySku(sku).orElseThrow(
                () -> new ProductNotFoundException(sku));

        product.setEnabled(false);
        productRepository.save(product);

        log.info("Product disabled {}", sku);
    }

    public void enableProduct(String sku) {
        Product product = productRepository.findBySku(sku).orElseThrow(
                () -> new ProductNotFoundException(sku));

        product.setEnabled(true);
        productRepository.save(product);

        log.info("Product enabled {}", sku);
    }

    public Page<AdminProductResponse> getAdminProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(AdminProductResponse::from);
    }

    public void deleteProducts(List<String> skus) {
        List<Product> products = productRepository.findAllBySkuIn(skus);
        if (products.isEmpty()) {
            return;
        }
        productRepository.deleteAll(products);
        log.info("Deleted products: {}", skus);
    }

    //========Public=======
    public Page<ProductResponse> getPublicProducts(Pageable pageable) {
        return productRepository
                .findAllByEnabledTrue(pageable)
                .map(ProductResponse::from);
    }

    public ProductResponse getPublicProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .filter(Product::isEnabled).orElseThrow(
                        () -> new ProductNotFoundException(sku));

        return ProductResponse.from(product);
    }
}
