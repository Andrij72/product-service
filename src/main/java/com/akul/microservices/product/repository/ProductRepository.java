package com.akul.microservices.product.repository;

import com.akul.microservices.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * ProductRepository.java.
 *
 * @author Andrii Kulynch
 * @version 1.0
 * @since 8/19/2025
 */
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    Page<Product> findAllByEnabledTrue(Pageable pageable);

    List<Product> findAllBySkuIn(List<String> skus);

}
