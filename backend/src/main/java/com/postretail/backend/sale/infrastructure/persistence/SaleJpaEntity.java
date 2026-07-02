package com.postretail.backend.sale.infrastructure.persistence;

import com.postretail.backend.sale.domain.model.SalePayment;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sales")
public class SaleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "cash_session_id", nullable = false)
    private Long cashSessionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Set + LinkedHashSet: Hibernate deduplica el producto cartesiano que
    // se produce al traer dos colecciones en el mismo EntityGraph, conservand
    // el orden de insercion.
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SaleItemJpaEntity> items = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SalePaymentJpaEntity> payments = new LinkedHashSet<>();

    public SaleJpaEntity() {}

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void addItem(SaleItemJpaEntity item) {
        items.add(item);
        item.setSale(this);
    }

    public void addPayment(SalePaymentJpaEntity payment) {
        payments.add(payment);
        payment.setSale(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getCashSessionId() {
        return cashSessionId;
    }

    public void setCashSessionId(Long cashSessionId) {
        this.cashSessionId = cashSessionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<SaleItemJpaEntity> getItems() {
        return items;
    }

    public void setItems(Set<SaleItemJpaEntity> items) {
        this.items = items;
    }

    public Set<SalePaymentJpaEntity> getPayments() {
        return payments;
    }

    public void setPayments(Set<SalePaymentJpaEntity> payments) {
        this.payments = payments;
    }
}
