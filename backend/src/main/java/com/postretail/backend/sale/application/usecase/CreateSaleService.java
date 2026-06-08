package com.postretail.backend.sale.application.usecase;

import com.postretail.backend.product.domain.model.Product;
import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.model.SaleItem;
import com.postretail.backend.sale.domain.port.in.CreateSaleUseCase;
import com.postretail.backend.sale.domain.port.out.LoadProductPort;
import com.postretail.backend.sale.domain.port.out.SaleRepository;
import com.postretail.backend.sale.domain.port.out.UpdateStockPort;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

public class CreateSaleService implements CreateSaleUseCase {

    private final LoadProductPort loadProductPort;
    private final UpdateStockPort updateStockPort;
    private final SaleRepository saleRepository;

    public CreateSaleService(LoadProductPort loadProductPort, UpdateStockPort updateStockPort, SaleRepository saleRepository) {
        this.loadProductPort = loadProductPort;
        this.updateStockPort = updateStockPort;
        this.saleRepository = saleRepository;
    }


    @Override
    @Transactional
    public Sale createSale(CreateSaleCommand command) {
        List<SaleItem> items = new ArrayList<>();

        // 1. Por cas linea: cargar el producto real y validar
        for (CreateSaleCommand.SaleLine line : command.items()) {
            Product product = loadProductPort
                    .loadById(line.productId())
                    .orElseThrow(() -> new  IllegalArgumentException(
                            "Product not found: " + line.productId()
                    ));

            if (!product.isActive()) {
                throw new IllegalStateException(
                        "Product is not active: " + product.getSku()
                );
            }

            if (product.getStock() < line.quantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for product " + product.getSku()
                        + " (available: " + product.getStock()
                        + ", requested: " + line.quantity() + ")"
                );
            }

            // El precio se toma del producto real, NO del cliente
            items.add(
                    new SaleItem(
                            product.getId(),
                            line.quantity(),
                            product.getPrice()
                    )
            );
        }

        // 2. Construir la venta del dominio (calcula el total solo)
        Sale sale = new Sale(null, items, command.paymentMethodId());

        // 3. Descontar stock de cada producto
        for (SaleItem item : items) {
            updateStockPort.decreaseStock(item.getProductId(), item.getQuantity());
        }

        // 4. Persistir la venta y devolverla
        return saleRepository.save(sale);
    }
}
