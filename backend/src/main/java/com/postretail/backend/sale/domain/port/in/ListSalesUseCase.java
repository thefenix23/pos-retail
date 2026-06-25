package com.postretail.backend.sale.domain.port.in;

import com.postretail.backend.sale.domain.model.Sale;

import java.util.List;

public interface ListSalesUseCase {
    List<Sale> findAll();
}
