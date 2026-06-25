package com.postretail.backend.sale.domain.port.in;

import com.postretail.backend.sale.domain.model.Sale;

import java.util.Optional;

public interface GetSaleUseCase {
    Optional<Sale> findById(Long id);
}
