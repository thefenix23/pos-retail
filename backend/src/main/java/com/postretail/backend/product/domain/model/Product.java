package com.postretail.backend.product.domain.model;

import java.math.BigDecimal;

public class Product {

    private final Long id;
    private final String sku;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final int stock;
    private final boolean active;
    private final Long categoryId;

    public Product(Long id, String sku, String name, String description, BigDecimal price, int stock, boolean active, Long categoryId) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.active = active;
        this.categoryId = categoryId;
    }

    public boolean isAvailable() {
        return active && stock > 0;
    }

    public Product winthId(Long newId) {
        return new Product(newId, sku, name, description, price, stock, active, categoryId);
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isActive() {
        return active;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
