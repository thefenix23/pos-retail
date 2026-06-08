package com.postretail.backend.sale.infrastructure.config;

import com.postretail.backend.sale.application.usecase.CreateSaleService;
import com.postretail.backend.sale.domain.port.in.CreateSaleUseCase;
import com.postretail.backend.sale.domain.port.out.LoadProductPort;
import com.postretail.backend.sale.domain.port.out.SaleRepository;
import com.postretail.backend.sale.domain.port.out.UpdateStockPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaleConfig {

    @Bean
    public CreateSaleUseCase createSaleUseCase(
            LoadProductPort loadProductPort,
            UpdateStockPort updateStockPort,
            SaleRepository saleRepository
    ) {
        return new CreateSaleService(
                loadProductPort,
                updateStockPort,
                saleRepository
        );
    }
}
