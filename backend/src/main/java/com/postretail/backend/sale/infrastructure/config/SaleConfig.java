package com.postretail.backend.sale.infrastructure.config;

import com.postretail.backend.sale.application.usecase.CreateSaleService;
import com.postretail.backend.sale.application.usecase.GetSaleService;
import com.postretail.backend.sale.application.usecase.ListSalesService;
import com.postretail.backend.sale.domain.port.in.CreateSaleUseCase;
import com.postretail.backend.sale.domain.port.in.GetSaleUseCase;
import com.postretail.backend.sale.domain.port.in.ListSalesUseCase;
import com.postretail.backend.sale.domain.port.out.CashSessionStatusPort;
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
            SaleRepository saleRepository,
            CashSessionStatusPort cashSessionStatusPort
    ) {
        return new CreateSaleService(
                loadProductPort,
                updateStockPort,
                saleRepository,
                cashSessionStatusPort
        );
    }

    @Bean
    public ListSalesUseCase listSalesUseCase(SaleRepository saleRepository) {
        return new ListSalesService(saleRepository);
    }

    @Bean
    public GetSaleUseCase getSaleUseCase(SaleRepository saleRepository) {
        return new GetSaleService(saleRepository);
    }
}
