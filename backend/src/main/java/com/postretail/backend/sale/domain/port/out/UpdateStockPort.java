package com.postretail.backend.sale.domain.port.out;

public interface UpdateStockPort {
    void decreaseStock(Long productId, int quantity);
}
