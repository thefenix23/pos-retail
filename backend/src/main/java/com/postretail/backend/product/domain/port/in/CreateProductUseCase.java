package com.postretail.backend.product.domain.port.in;

import com.postretail.backend.product.domain.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface CreateProductUseCase {
    Product create(Product product);
}
