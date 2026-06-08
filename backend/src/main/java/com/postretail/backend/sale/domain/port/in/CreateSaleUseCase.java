package com.postretail.backend.sale.domain.port.in;

import com.postretail.backend.sale.domain.model.Sale;

import java.util.List;

public interface CreateSaleUseCase {

    Sale createSale(CreateSaleCommand command);

    record CreateSaleCommand(
            List<SaleLine> items,
            Long paymentMethodId
    ) {
        public record SaleLine(Long productId, int quantity) {}
    }
}
