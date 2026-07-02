package com.postretail.backend.cashsession.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_sessions")
public class CashSessionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "register_id", nullable = false)
    private Long registerId;

    @Column(name = "cashier", nullable = false, length = 100)
    private String cashier;

    @Column(name = "initial_fund", nullable = false, precision = 12, scale = 2)
    private BigDecimal initialFund;

    @Column(name = "status", nullable = false, length = 10)
    private String status;

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    // Requerido por JPA
    protected CashSessionJpaEntity() {}

    public CashSessionJpaEntity(Long id, Long branchId, Long registerId, String cashier, BigDecimal initialFund, String status, LocalDateTime openedAt, LocalDateTime closedAt) {
        this.id = id;
        this.branchId = branchId;
        this.registerId = registerId;
        this.cashier = cashier;
        this.initialFund = initialFund;
        this.status = status;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getBranchId() {
        return branchId;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public String getCashier() {
        return cashier;
    }

    public BigDecimal getInitialFund() {
        return initialFund;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }
}
