package com.postretail.backend.sale.domain.port.out;

import com.postretail.backend.sale.domain.model.Sale;

public interface SaleRepository {
    Sale save(Sale sale);
}
