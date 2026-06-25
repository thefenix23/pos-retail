package com.postretail.backend.paymentmethod.infrastructure.persistence;

import com.postretail.backend.paymentmethod.domain.model.PaymentMethod;

public class PaymentMethodMapper {

    private PaymentMethodMapper() {}

    public static PaymentMethod toDomain(PaymentMethodJpaEntity entity) {
        return new PaymentMethod(
                entity.getId(),
                entity.getName(),
                entity.isActive()
        );
    }
}
