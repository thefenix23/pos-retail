package com.postretail.backend.product.infrastructure.web;

import com.postretail.backend.product.domain.port.in.*;
import com.postretail.backend.product.infrastructure.web.dto.CreateProductRequest;
import com.postretail.backend.product.infrastructure.web.dto.ProductResponse;
import com.postretail.backend.product.infrastructure.web.dto.UpdateProductRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final SearchProductsUseCase searchProductsUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(SearchProductsUseCase searchProductsUseCase, ListProductsUseCase listProductsUseCase, CreateProductUseCase createProductUseCase, UpdateProductUseCase updateProductUseCase, DeleteProductUseCase deleteProductUseCase) {
        this.searchProductsUseCase = searchProductsUseCase;
        this.listProductsUseCase = listProductsUseCase;
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    // ------ LECTURA ------

    // Lista todos los productos (para la pantalla de administración
    @GetMapping
    public List<ProductResponse> findAll() {
        return listProductsUseCase
                .findAll()
                .stream()
                .map(ProductResponse::fromDomain)
                .toList();
    }

    @GetMapping("/search")
    public List<ProductResponse> search(@RequestParam String query) {
        return searchProductsUseCase
                .searchByNameOrSku(query)
                .stream()
                .map(ProductResponse::fromDomain)
                .toList();
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> findBySku(@PathVariable String sku) {
        return searchProductsUseCase
                .findBySku(sku)
                .map(ProductResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    // ------ ESCRITURA ------

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        var created = createProductUseCase
                .create(
                        request.toDomain()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductResponse.fromDomain(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        var updated = updateProductUseCase
                .update(
                        id, request.toDomain()
                );

        return ResponseEntity.ok(
                ProductResponse
                        .fromDomain(updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteProductUseCase.delete(id);

        return ResponseEntity.noContent().build();
    }
}
