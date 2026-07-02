package com.postretail.backend.cashsession.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_movements")
public class CashMovementJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cash_session_id", nullable = false)
    private Long cashSessionId;

    @Column(name = "type", nullable = false, length = 10)
    private String type;

    @Column(name = "concept", nullable = false, length = 255)
    private String concept;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "responsible", nullable = false, length = 100)
    private String responsible;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected CashMovementJpaEntity() {}

    public CashMovementJpaEntity(Long id, Long cashSessionId, String type, String concept, BigDecimal amount,String responsible, LocalDateTime createdAt) {
        this.id = id;
        this.cashSessionId = cashSessionId;
        this.type = type;
        this.concept = concept;
        this.amount = amount;
        this.responsible = responsible;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCashSessionId() {
        return cashSessionId;
    }

    public String getType() {
        return type;
    }

    public String getConcept() {
        return concept;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getResponsible() {
        return responsible;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
