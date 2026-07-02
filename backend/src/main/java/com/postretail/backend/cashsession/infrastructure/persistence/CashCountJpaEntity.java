package com.postretail.backend.cashsession.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_counts")
public class CashCountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cash_session_id", nullable = false, unique = true)
    private Long cashSessionId;

    @Column(name = "expected_cash", nullable = false, precision = 12, scale = 2)
    private BigDecimal expectedCash;

    @Column(name = "counted_cash", nullable = false, precision = 12,  scale = 2)
    private BigDecimal countedCash;

    @Column(name = "difference", nullable = false, precision = 12, scale = 2)
    private BigDecimal difference;

    @Column(name = "approved_by", nullable = false, length = 100)
    private String approvedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected CashCountJpaEntity() {}

    public CashCountJpaEntity(Long id, Long cashSessionId, BigDecimal expectedCash, BigDecimal countedCash, BigDecimal difference, String approvedBy, LocalDateTime createdAt) {
        this.id = id;
        this.cashSessionId = cashSessionId;
        this.expectedCash = expectedCash;
        this.countedCash = countedCash;
        this.difference = difference;
        this.approvedBy = approvedBy;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCashSessionId() {
        return cashSessionId;
    }

    public BigDecimal getExpectedCash() {
        return expectedCash;
    }

    public BigDecimal getCountedCash() {
        return countedCash;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
