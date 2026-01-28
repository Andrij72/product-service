package com.akul.microservices.product.service;

import com.akul.microservices.product.client.FileServiceRestClient;
import com.akul.microservices.product.dto.AdminProductResponse;
import com.akul.microservices.product.dto.FileDto;
import com.akul.microservices.product.dto.ProductRequest;
import com.akul.microservices.product.dto.ProductResponse;
import com.akul.microservices.product.dto.ProductUpdateRequest;
import com.akul.microservices.product.exception.ProductAlreadyExistsException;
import com.akul.microservices.product.exception.ProductNotFoundException;
import com.akul.microservices.product.model.Product;
import com.akul.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileServiceRestClient fileServiceRestClient;

    public AdminProductResponse createAdminProduct(ProductRequest request) {
        return createAdminProduct(request, null);
    }

    public AdminProductResponse createAdminProduct(
            ProductRequest request, MultipartFile file) {
        if (productRepository.existsBySku(request.sku())) {
            throw new ProductAlreadyExistsException(request.sku());
        }

        Product product = Product.builder()
                .sku(request.sku())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .enabled(true)
                .build();

        if (file != null && !file.isEmpty()) {
            FileDto dto = fileServiceRestClient.uploadProductImage(
                    product.getSku(), file);
            product.setImageObjectName(dto.objectName());

            Product saved = productRepository.save(product);
            return AdminProductResponse.from(saved,
                    dto.presignedUrl());
        }

        Product saved = productRepository.save(product);
        return AdminProductResponse.from(saved, null);
    }


    public AdminProductResponse updateAdminProduct(
            String sku,
            ProductUpdateRequest request,
            MultipartFile file
    ) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());

        if (file != null && !file.isEmpty()) {
            FileDto dto = fileServiceRestClient.uploadProductImage(sku, file);
            product.setImageObjectName(dto.objectName());

            Product saved = productRepository.save(product);
            return AdminProductResponse.from(saved, dto.presignedUrl());
        }

        Product saved = productRepository.save(product);

        String previewUrl = saved.getImageObjectName() != null
                ? fileServiceRestClient.getPreviewUrl(
                        saved.getImageObjectName())
                : null;

        return AdminProductResponse.from(saved, previewUrl);
    }

    /**
     * Update only the image of an existing product.
     *
     * @param sku  the SKU of the product
     * @param file the new image file
     * @return AdminProductResponse with updated image info
     */
    public AdminProductResponse updateProductImage(String sku,
                                                   MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Image file must not be empty");
        }

        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        FileDto dto = fileServiceRestClient.uploadProductImage(sku, file);

        product.setImageObjectName(dto.objectName());
        Product saved = productRepository.save(product);

        return AdminProductResponse.from(saved, dto.presignedUrl());
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
                .map(product -> {
                    String previewUrl = null;
                    if (product.getImageObjectName() != null) {
                        previewUrl = fileServiceRestClient.getPreviewUrl(
                                product.getImageObjectName());
                    }
                    return AdminProductResponse.from(product, previewUrl);
                });
    }

    public void deleteProducts(List<String> skus) {
        List<Product> products = productRepository.findAllBySkuIn(skus);
        if (products.isEmpty()) {
            return;
        }
        productRepository.deleteAll(products);
        log.info("Deleted products: {}", skus);
    }


    public Page<ProductResponse> getPublicProducts(Pageable pageable) {
        return productRepository
                .findAllByEnabledTrue(pageable)
                .map(product -> {
                    String imageUrl = fileServiceRestClient.getPreviewUrl(
                            product.getImageObjectName()
                    );
                    return ProductResponse.from(product, imageUrl);
                });
    }

    public ProductResponse getPublicProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .filter(Product::isEnabled)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        String imageUrl = fileServiceRestClient.getPreviewUrl(
                product.getImageObjectName()
        );

        return ProductResponse.from(product, imageUrl);
    }

    public AdminProductResponse getAdminProduct(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        String previewUrl = null;
        if (product.getImageObjectName() != null) {
            previewUrl = fileServiceRestClient.getPreviewUrl(
                    product.getImageObjectName());
        }

        return AdminProductResponse.from(product, previewUrl);
    }
}
