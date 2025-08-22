package com.akul.microservices.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Product.java
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */

@Document(collection = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private Double price;
}
