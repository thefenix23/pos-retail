package com.postretail.backend.product.infrastructure.web;

import com.postretail.backend.product.domain.port.in.SearchProductsUseCase;
import com.postretail.backend.product.infrastructure.web.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final SearchProductsUseCase searchProductsUseCase;

    public ProductController(SearchProductsUseCase searchProductsUseCase) {
        this.searchProductsUseCase = searchProductsUseCase;
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
                .orElse(ResponseEntity.notFound().build());
    }
}
