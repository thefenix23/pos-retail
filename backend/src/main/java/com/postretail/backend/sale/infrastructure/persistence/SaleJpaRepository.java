package com.postretail.backend.sale.infrastructure.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleJpaRepository extends JpaRepository<SaleJpaEntity, Long> {
    // Trae las ventas más recientes primero, cargando los items en la misma consulta
    // (EntityGraph evita el problema N+1 y el LazyInitializationException)
    @EntityGraph(attributePaths = "items")
    List<SaleJpaEntity> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "items")
    Optional<SaleJpaEntity> findWithItemsById(Long id);
}
