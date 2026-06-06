package com.postretail.backend;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ping")
public class Ping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String msg;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
