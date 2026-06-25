package com.postretail.backend.paymentmethod.infrastructure.web.dto;

import com.postretail.backend.paymentmethod.domain.model.PaymentMethod;

public record PaymentMethodResponse(
        Long id,
        String name
) {
    public static PaymentMethodResponse fromDomain(PaymentMethod paymentMethod) {
        return new PaymentMethodResponse(
                paymentMethod.getId(),
                paymentMethod.getName()
        );
    }
}