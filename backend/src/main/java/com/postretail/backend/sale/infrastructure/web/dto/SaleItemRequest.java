package com.postretail.backend.sale.infrastructure.web.dto;

public record SaleItemRequest (
    Long productId,
    int quantity
) {}
