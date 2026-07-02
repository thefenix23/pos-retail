package com.postretail.backend.sale.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas del dominio Sale.
 * Lógica pura: sin Spring, sin base de datos. Verifica el cálculo del total,
 * el cálculo del cambio y las reglas de negocio (items, pagos, caja, cobertura del total).
 */
public class SaleTest {

    // Helper: un pago en efectivo del monto indicado (método de pago 1).
    private SalePayment efectivo(String monto) {
        return new SalePayment(1L, new BigDecimal(monto));
    }

    @Test
    void calculaElTotalSumandoLosSubtotalesDeCadaItem() {
        // Dos productos: 2 x $25.50 = $51.00, y 3 x $10 = $30.00 -> total $81.00
        SaleItem item1 = new SaleItem(1L, 2, new BigDecimal("25.50"));
        SaleItem item2 = new SaleItem(2L, 3, new BigDecimal("10.00"));

        Sale sale = new Sale(null, List.of(item1, item2), List.of(efectivo("81.00")), 1L);

        assertEquals(0, sale.getTotal().compareTo(new BigDecimal("81.00")),
                "El total debe ser la suma de los subtotales de cada línea");
    }

    @Test
    void elTotalDeUnSoloProductoEsSuSubtotal() {
        // 7 x $1.00 = $7.00
        SaleItem item = new SaleItem(23L, 7, new BigDecimal("1.00"));

        Sale sale = new Sale(null, List.of(item), List.of(efectivo("7.00")), 1L);

        assertEquals(0, sale.getTotal().compareTo(new BigDecimal("7.00")));
    }

    @Test
    void unaVentaNuevaQuedaEnEstadoCompleted() {
        SaleItem item = new SaleItem(1L, 1, new BigDecimal("50.00"));

        Sale sale = new Sale(null, List.of(item), List.of(efectivo("50.00")), 1L);

        assertEquals("COMPLETED", sale.getStatus());
    }

    @Test
    void calculaElCambioCuandoElPagoSuperaElTotal() {
        // Total $50, paga $80 -> cambio $30
        SaleItem item = new SaleItem(1L, 1, new BigDecimal("50.00"));

        Sale sale = new Sale(null, List.of(item), List.of(efectivo("80.00")), 1L);

        assertEquals(0, sale.getChange().compareTo(new BigDecimal("30.00")),
                "El cambio debe ser lo pagado menos el total");
    }

    @Test
    void elCambioEsCeroCuandoElPagoEsExacto() {
        SaleItem item = new SaleItem(1L, 1, new BigDecimal("50.00"));

        Sale sale = new Sale(null, List.of(item), List.of(efectivo("50.00")), 1L);

        assertEquals(0, sale.getChange().compareTo(BigDecimal.ZERO));
    }

    @Test
    void sumaVariosPagosParaCubrirElTotal() {
        // Total $100, paga $60 efectivo + $40 tarjeta -> cubierto, cambio $0
        SaleItem item = new SaleItem(1L, 2, new BigDecimal("50.00"));
        SalePayment pagoEfectivo = new SalePayment(1L, new BigDecimal("60.00"));
        SalePayment pagoTarjeta = new SalePayment(2L, new BigDecimal("40.00"));

        Sale sale = new Sale(null, List.of(item), List.of(pagoEfectivo, pagoTarjeta), 1L);

        assertEquals(0, sale.getPaidAmount().compareTo(new BigDecimal("100.00")));
        assertEquals(0, sale.getChange().compareTo(BigDecimal.ZERO));
    }

    @Test
    void noSePuedeCrearUnaVentaSinItems() {
        // Una venta sin líneas no tiene sentido de negocio: debe rechazarse
        assertThrows(
                IllegalArgumentException.class,
                () -> new Sale(null, List.of(), List.of(efectivo("10.00")), 1L),
                "Una venta sin items debe lanzar excepción"
        );
    }

    @Test
    void noSePuedeCrearUnaVentaSinPagos() {
        SaleItem item = new SaleItem(1L, 1, new BigDecimal("50.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Sale(null, List.of(item), List.of(), 1L),
                "Una venta sin pagos debe lanzar excepción"
        );
    }

    @Test
    void noSePuedeCrearUnaVentaSinCaja() {
        SaleItem item = new SaleItem(1L, 1, new BigDecimal("50.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Sale(null, List.of(item), List.of(efectivo("50.00")), null),
                "Una venta sin sesión de caja debe lanzar excepción"
        );
    }

    @Test
    void noSePuedeCrearUnaVentaSiLosPagosNoCubrenElTotal() {
        // Total $50, paga solo $30 -> debe rechazarse
        SaleItem item = new SaleItem(1L, 1, new BigDecimal("50.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Sale(null, List.of(item), List.of(efectivo("30.00")), 1L),
                "Si los pagos no cubren el total debe lanzar excepción"
        );
    }
}