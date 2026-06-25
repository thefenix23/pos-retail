package com.postretail.backend.paymentmethod.domain.model;

public class PaymentMethod {

    private final Long id;
    private final String name;
    private final boolean active;

    public PaymentMethod(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }
}
