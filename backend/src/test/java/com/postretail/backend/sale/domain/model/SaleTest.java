package com.postretail.backend.sale.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas del dominio Sale.
 * Lógica pura: sin Spring, sin base de datos. Verifica el cálculo del total
 * y las reglas de negocio (una venta válida necesita items y método de pago).
 */

public class SaleTest {

    @Test
    void calculaElTotalSumandoLosSubtotalesDeCadaItem() {
        // Dos productos: 2 x $25.50 = $51.00, y 3 x $10 = $30.00 -> total $81.00
        SaleItem item1 = new SaleItem(1L, 2, new BigDecimal("25.50"));
        SaleItem item2 = new SaleItem(2L, 3, new BigDecimal("10.00"));

        Sale sale = new Sale(null, List.of(item1, item2), 1L);

        assertEquals(0, sale.getTotal().compareTo(new BigDecimal("81.00")),
                "El total debe ser la suma de los subtotales de cada línea");
    }

    @Test
    void elTotalDeUnSoloProductoEsSuSubtotal() {
        // 7 x $1.00 = $7.00
        SaleItem item = new SaleItem(23L, 7, new BigDecimal("1.00"));

        Sale sale = new Sale(null, List.of(item), 1L);

        assertEquals(0, sale.getTotal().compareTo(new BigDecimal("7.00")));
    }

    @Test
    void unaVentaNuevaQuedaEnEstadoCompleted() {
        SaleItem item = new SaleItem(1L, 1, new BigDecimal("50.00"));

        Sale sale = new Sale(null, List.of(item), 1L);

        assertEquals("COMPLETED", sale.getStatus());
    }

    @Test
    void noSePuedeCrearUnaVentaSinItems() {
        // Una venta sin líneas no tiene sentido de negocio: debe rechazarse
        assertThrows(
                IllegalArgumentException.class,
                () -> new Sale(null, List.of(), 1L),
                "Una venta sin items debe lanzar excepción"
        );
    }

    @Test
    void noSePuedeCrearUnaVentaSinMetodoDePago() {
        SaleItem item = new SaleItem(1L, 1, new BigDecimal("50.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Sale(null, List.of(item), null),
                "Una venta sin método de pago debe lanzar excepción"
        );
    }
}
