package com.postretail.backend.product.infrastructure.config;

import com.postretail.backend.product.application.usecase.SearchProductsService;
import com.postretail.backend.product.domain.port.in.SearchProductsUseCase;
import com.postretail.backend.product.domain.port.out.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    @Bean
    public SearchProductsUseCase searchProductsUseCase(ProductRepository productRepository) {
        return new SearchProductsService(productRepository);
    }
}
