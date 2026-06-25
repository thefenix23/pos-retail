package com.postretail.backend.paymentmethod.config;

import com.postretail.backend.paymentmethod.application.usecase.ListPaymentMethodsService;
import com.postretail.backend.paymentmethod.domain.port.in.ListPaymentMethodsUseCase;
import com.postretail.backend.paymentmethod.domain.port.out.PaymentMethodRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentMethodConfig {

    @Bean
    public ListPaymentMethodsUseCase listPaymentMethodsUseCase(PaymentMethodRepository paymentMethodRepository) {
        return new ListPaymentMethodsService(paymentMethodRepository);
    }
}
