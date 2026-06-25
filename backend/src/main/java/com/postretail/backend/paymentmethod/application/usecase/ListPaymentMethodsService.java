package com.postretail.backend.paymentmethod.application.usecase;

import com.postretail.backend.paymentmethod.domain.model.PaymentMethod;
import com.postretail.backend.paymentmethod.domain.port.in.ListPaymentMethodsUseCase;
import com.postretail.backend.paymentmethod.domain.port.out.PaymentMethodRepository;

import java.util.List;

public class ListPaymentMethodsService implements ListPaymentMethodsUseCase {

    private final PaymentMethodRepository paymentMethodRepository;

    public ListPaymentMethodsService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<PaymentMethod> findAllActive() {
        return paymentMethodRepository.findAllActive();
    }
}
