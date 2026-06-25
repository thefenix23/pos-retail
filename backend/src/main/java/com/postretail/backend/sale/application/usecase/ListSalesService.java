package com.postretail.backend.sale.application.usecase;

import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.port.in.ListSalesUseCase;
import com.postretail.backend.sale.domain.port.out.SaleRepository;

import java.util.List;

public class ListSalesService implements ListSalesUseCase {

    private final SaleRepository saleRepository;

    public ListSalesService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }
}
