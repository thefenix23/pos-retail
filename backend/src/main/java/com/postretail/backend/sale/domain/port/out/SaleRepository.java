package com.postretail.backend.sale.domain.port.out;

import com.postretail.backend.sale.domain.model.Sale;

import java.util.List;
import java.util.Optional;

public interface SaleRepository {
    Sale save(Sale sale);
    List<Sale> findAll();
    Optional<Sale> findById(Long id);
}
