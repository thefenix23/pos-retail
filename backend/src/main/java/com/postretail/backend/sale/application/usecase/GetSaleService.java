package com.postretail.backend.sale.application.usecase;

import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.port.in.GetSaleUseCase;
import com.postretail.backend.sale.domain.port.out.SaleRepository;

import java.util.Optional;

public class GetSaleService implements GetSaleUseCase {

    private final SaleRepository saleRepository;

    public GetSaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }
}
