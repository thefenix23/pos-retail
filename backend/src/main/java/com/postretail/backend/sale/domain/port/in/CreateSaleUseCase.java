package com.postretail.backend.sale.domain.port.in;

import com.postretail.backend.sale.domain.model.Sale;

import java.math.BigDecimal;
import java.util.List;

public interface CreateSaleUseCase {

    Sale createSale(CreateSaleCommand command);

    record CreateSaleCommand(
            List<SaleLine> items,
            List<PaymentLine> paymets
    ) {
        public record SaleLine(Long productId, int quantity) {}

        public record PaymentLine(Long paymentMethodId, BigDecimal amount) {}
    }
}
