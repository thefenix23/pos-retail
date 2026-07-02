package com.postretail.backend.sale.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "sale_payments")
public class SalePaymentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private SaleJpaEntity sale;

    @Column(name = "payment_method_id", nullable = false)
    private Long paymentMethodId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    public SalePaymentJpaEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleJpaEntity getSale() {
        return sale;
    }

    public void setSale(SaleJpaEntity sale) {
        this.sale = sale;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // equals/hashCode por id para comportarse bien adentro de un Set.
    // Mientras id es null (antes de persistir) cada instancia es unica.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalePaymentJpaEntity other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
