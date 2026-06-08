package com.postretail.backend.sale.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleJpaRepository extends JpaRepository<SaleJpaEntity, Long> {
}
