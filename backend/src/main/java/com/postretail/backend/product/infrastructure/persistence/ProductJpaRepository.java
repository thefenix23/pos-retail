package com.postretail.backend.product.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    @Query("""
            SELECT p FROM ProductJpaEntity p
            WHERE p.active = true
            AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%')))
            ORDER BY p.name ASC
            """)

    List<ProductJpaEntity> searchByNameOrSku(@Param("query") String query);

    Optional<ProductJpaEntity> findBySkuAndActiveTrue(String sku);
}
