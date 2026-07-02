package com.postretail.backend.sale.domain.model;

import java.math.BigDecimal;

public class SalePayment {

    private final Long paymentMethodId;
    private final BigDecimal amount;

    public SalePayment(Long paymentMethodId, BigDecimal amount) {
        if (paymentMethodId == null) {
            throw new IllegalArgumentException("Se requiere un método de pago");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }

        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
