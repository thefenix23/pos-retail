package com.postretail.backend.paymentmethod.infrastructure.web;

import com.postretail.backend.paymentmethod.domain.port.in.ListPaymentMethodsUseCase;
import com.postretail.backend.paymentmethod.infrastructure.web.dto.PaymentMethodResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final ListPaymentMethodsUseCase listPaymentMethodsUseCase;

    public PaymentMethodController(ListPaymentMethodsUseCase listPaymentMethodsUseCase) {
        this.listPaymentMethodsUseCase = listPaymentMethodsUseCase;
    }

    @GetMapping
    public List<PaymentMethodResponse> findAll() {
        return listPaymentMethodsUseCase
                .findAllActive()
                .stream()
                .map(PaymentMethodResponse::fromDomain)
                .toList();
    }
}
