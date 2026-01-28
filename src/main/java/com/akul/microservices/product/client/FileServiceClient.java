package com.akul.microservices.product.client;

import com.akul.microservices.product.dto.FileDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
/**
 * FileServiceClient.java. *
 * Updated to work with new FileController endpoints
 */
public interface FileServiceClient {

    /**
     * Uploads a product image by SKU.
     * Calls new endpoint in FileService:
     * POST /api/v1/files/upload/product/{sku}
     * @param file multipart file
     * @param sku  SKU of the product
     * @return metadata of uploaded file
     */
    @PostExchange(
            value = "/api/v1/files/upload/product/{sku}",
            contentType = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    FileDto uploadProductImage(
            @PathVariable("sku") String sku,
            @RequestPart("file") MultipartFile file
    );

    /**
     * Gets preview URL for product image.
     *
     * @param objectName name of the object in storage
     * @return presigned URL
     */
    @GetExchange("/api/v1/files/preview")
    String getPreviewUrl(@RequestParam("objectName") String objectName);
}
