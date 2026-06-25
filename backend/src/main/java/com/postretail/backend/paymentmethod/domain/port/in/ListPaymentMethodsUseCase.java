package com.postretail.backend.paymentmethod.domain.port.in;

import com.postretail.backend.paymentmethod.domain.model.PaymentMethod;

import java.util.List;

public interface ListPaymentMethodsUseCase {
    List<PaymentMethod> findAllActive();
}
