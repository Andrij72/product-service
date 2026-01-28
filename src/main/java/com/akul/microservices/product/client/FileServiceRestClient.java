package com.akul.microservices.product.client;

import com.akul.microservices.product.dto.FileDto;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST client for fetching product images from File-service.
 * <p>
 * Handles real files with fallback to placeholder in case of errors.
 * <p>
 * Author: Andrii Kulynych
 * Since: 1/26/2026
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class FileServiceRestClient {

    private static final String PLACEHOLDER_IMAGE = "/images/placeholder.jpg";

    private final FileServiceClient client;

    @CircuitBreaker(name = "fileServiceCB", fallbackMethod = "previewFallback")
    @Retry(name = "fileServiceCB")
    @Bulkhead(name = "fileServiceCB")
    public String getPreviewUrl(String objectName) {
        try {
            return client.getPreviewUrl(objectName);
        } catch (HttpClientErrorException.NotFound ex) {
            log.debug("Image not found: {}", objectName);
            return PLACEHOLDER_IMAGE;
        }
    }

    @SuppressWarnings("unused")
    public String previewFallback(String objectName,
                                  Throwable ex) {
        log.warn("File-service unavailable."
                 + " Using placeholder. objectName={}",
                objectName, ex);
        return PLACEHOLDER_IMAGE;
    }

    @CircuitBreaker(name = "fileServiceCB")
    @Retry(name = "fileServiceCB")
    @Bulkhead(name = "fileServiceCB")
    public FileDto uploadProductImage(String sku,
                                      MultipartFile file) {
        FileDto response = client.uploadProductImage(
                sku, file);
        log.info("Image uploaded to file-service: {}",
                response.objectName());

        return new FileDto(response.objectName(),
                file.getContentType(),
                file.getSize(),
                response.presignedUrl());
    }
}
