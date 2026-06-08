package com.postretail.backend.sale.infrastructure.web;

import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.port.in.CreateSaleUseCase;
import com.postretail.backend.sale.infrastructure.web.dto.CreateSaleRequest;
import com.postretail.backend.sale.infrastructure.web.dto.SaleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final CreateSaleUseCase createSaleUseCase;

    public SaleController(CreateSaleUseCase createSaleUseCase) {
        this.createSaleUseCase = createSaleUseCase;
    }

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody CreateSaleRequest request) {
        var command = new CreateSaleUseCase
                .CreateSaleCommand(
                        request
                                .items()
                                .stream()
                                .map(
                                        i -> new CreateSaleUseCase
                                                .CreateSaleCommand
                                                .SaleLine(
                                                        i.productId(),
                                                i.quantity())
                                )
                                .toList(),
                request.paymentMethodId()
        );

        Sale sale = createSaleUseCase.createSale(command);

        return ResponseEntity.status(201).body(SaleResponse.fromDomain(sale));
    }
}
