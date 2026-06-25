package com.postretail.backend.product.domain.exception;

public class DuplicateSkuException extends RuntimeException {
    public DuplicateSkuException(String sku) {
        super("Ya existe un producto con el sku: " + sku);
    }
}
