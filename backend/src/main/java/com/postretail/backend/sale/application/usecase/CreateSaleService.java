package com.postretail.backend.sale.application.usecase;

import com.postretail.backend.product.domain.model.Product;
import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.model.SaleItem;
import com.postretail.backend.sale.domain.model.SalePayment;
import com.postretail.backend.sale.domain.port.in.CreateSaleUseCase;
import com.postretail.backend.sale.domain.port.out.CashSessionStatusPort;
import com.postretail.backend.sale.domain.port.out.LoadProductPort;
import com.postretail.backend.sale.domain.port.out.SaleRepository;
import com.postretail.backend.sale.domain.port.out.UpdateStockPort;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

public class CreateSaleService implements CreateSaleUseCase {

    // Contexto de caja fijo por ahora. En fases futuras(login / multi-caja)
    // vendrá de la sessión del usuario aiutenticado.
    private static final Long BRANCH_ID = 1L;
    private static final Long REGISTER_ID = 1L;

    private final LoadProductPort loadProductPort;
    private final UpdateStockPort updateStockPort;
    private final SaleRepository saleRepository;
    private final CashSessionStatusPort cashSessionStatusPort;

    public CreateSaleService(LoadProductPort loadProductPort, UpdateStockPort updateStockPort, SaleRepository saleRepository, CashSessionStatusPort cashSessionStatusPort) {
        this.loadProductPort = loadProductPort;
        this.updateStockPort = updateStockPort;
        this.saleRepository = saleRepository;
        this.cashSessionStatusPort = cashSessionStatusPort;
    }


    @Override
    @Transactional
    public Sale createSale(CreateSaleCommand command) {
        // 0. No se puedde vender sin una caja abierta.
        Long cashSessionId = cashSessionStatusPort
                .findOpenSessionId(
                        BRANCH_ID,
                        REGISTER_ID
                );

        if (cashSessionId == null) {
            throw new IllegalStateException("No hay una caja abierta para registrar la venta");
        }

        List<SaleItem> items = new ArrayList<>();

        // 1. Por cada linea: cargar el producto real y validar
        for (CreateSaleCommand.SaleLine line : command.items()) {
            Product product = loadProductPort
                    .loadById(line.productId())
                    .orElseThrow(
                            () -> new IllegalArgumentException(
                                    "Product not found: " + line.productId()
                            )
                    );

            if (!product.isActive()) {
                throw new IllegalStateException(
                        "Insufficient stock for product " + product.getSku()
                        + " (available: " + product.getStock()
                        + ", request: " + line.quantity() + ")"
                );
            }

            // El precio se toma del producto real, No del cliente
            items.add(
                    new SaleItem(
                            product.getId(),
                            line.quantity(),
                            product.getPrice()
                    )
            );
        }

        // 2. Mapear los pagos del comando al dominio
        List<SalePayment> payments = command
                .paymets()
                .stream()
                .map(
                        p -> new SalePayment(
                                p.paymentMethodId(),
                                p.amount()
                        )
                )
                .toList();

        // 3. Construir la venta del dominio.
        // Aquí el dominio valida que los pagos cubran el total y calcula el cambio.
        Sale sale = new Sale(null, items, payments, cashSessionId);

        // 4. Descontar stock de cada producto
        for (SaleItem item : items) {
            updateStockPort.decreaseStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        // 5. Persistir la venta y devolverla
        return saleRepository.save(sale);
    }
}
