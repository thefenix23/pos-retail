package com.postretail.backend.sale.infrastructure.web;

import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.model.SaleItem;
import com.postretail.backend.sale.domain.port.in.CreateSaleUseCase;
import com.postretail.backend.sale.domain.port.in.GetSaleUseCase;
import com.postretail.backend.sale.domain.port.in.ListSalesUseCase;
import com.postretail.backend.sale.domain.port.out.LoadProductNamesPort;
import com.postretail.backend.sale.infrastructure.web.dto.CreateSaleRequest;
import com.postretail.backend.sale.infrastructure.web.dto.SaleDetailResponse;
import com.postretail.backend.sale.infrastructure.web.dto.SaleResponse;
import com.postretail.backend.sale.infrastructure.web.dto.SaleSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final CreateSaleUseCase createSaleUseCase;
    private final ListSalesUseCase listSalesUseCase;
    private final GetSaleUseCase getSaleUseCase;
    private final LoadProductNamesPort loadProductNamesPort;

    public SaleController(CreateSaleUseCase createSaleUseCase, ListSalesUseCase listSalesUseCase, GetSaleUseCase getSaleUseCase, LoadProductNamesPort loadProductNamesPort) {
        this.createSaleUseCase = createSaleUseCase;
        this.listSalesUseCase = listSalesUseCase;
        this.getSaleUseCase = getSaleUseCase;
        this.loadProductNamesPort = loadProductNamesPort;
    }

    // ------ Crear ------
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
                                                        i.quantity()
                                                )
                                )
                                .toList(),
                request
                        .payments()
                        .stream()
                        .map(
                                p -> new CreateSaleUseCase
                                        .CreateSaleCommand
                                        .PaymentLine(
                                                p.paymentMethodId(),
                                                p.amount()
                                        )
                        )
                        .toList()
        );

        Sale sale = createSaleUseCase.createSale(command);

        return ResponseEntity.status(201).body(SaleResponse.fromDomain(sale));
    }

    // ------ Listar ------
    @GetMapping
    public List<SaleSummaryResponse> findAll() {
        return listSalesUseCase
                .findAll()
                .stream()
                .map(SaleSummaryResponse::fromDomain)
                .toList();
    }

    // ------ Detalle de un aventa (con nombre de producto)
    @GetMapping("/{id}")
    public ResponseEntity<SaleDetailResponse> findById(@PathVariable Long id) {
        return getSaleUseCase
                .findById(id)
                .map(sale -> {
                    // Reúne los IDs de producto de la venta y carga sus nombres
                    List<Long> productIds = sale
                            .getItems()
                            .stream()
                            .map(SaleItem::getProductId)
                            .toList();

                    Map<Long, String> names = loadProductNamesPort
                            .loadNamesByIds(productIds);

                    return ResponseEntity.ok(
                            SaleDetailResponse
                                    .fromDomain(sale, names)
                    );
                })
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }
}
