package com.postretail.backend.paymentmethod.infrastructure.persistence;

import com.postretail.backend.paymentmethod.domain.model.PaymentMethod;
import com.postretail.backend.paymentmethod.domain.port.out.PaymentMethodRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMethodRepositoryAdapter implements PaymentMethodRepository {

    private final PaymentMethodJpaRepository jpaRepository;

    public PaymentMethodRepositoryAdapter(PaymentMethodJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<PaymentMethod> findAllActive() {
        return jpaRepository
                .findByActiveTrueOrderByIdAsc()
                .stream()
                .map(PaymentMethodMapper::toDomain)
                .toList();
    }
}
