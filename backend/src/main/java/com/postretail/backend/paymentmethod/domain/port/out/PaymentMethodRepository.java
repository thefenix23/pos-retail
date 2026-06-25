package com.postretail.backend.paymentmethod.domain.port.out;

import com.postretail.backend.paymentmethod.domain.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodRepository {
    List<PaymentMethod> findAllActive();
}
