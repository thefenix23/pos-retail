package com.postretail.backend.cashsession.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas del dominio CashCount (el corte de caja).
 * Lógica pura: verifica el cálculo del efectivo esperado y de la diferencia.
 *
 * esperado = fondo + ventas en efectivo + ingresos - egresos
 * diferencia = contado - esperado
 */
public class CashCountTest {

    @Test
    void calculaElEfectivoEsperadoConTodasLasFuentes() {
        // fondo 1500 + ventas 150 + ingresos 1000 - egresos 100 = 2550
        CashCount count = CashCount.generate(
                1L,
                new BigDecimal("1500.00"),  // fondo inicial
                new BigDecimal("150.00"),   // ventas en efectivo
                new BigDecimal("1000.00"),  // ingresos
                new BigDecimal("100.00"),   // egresos
                new BigDecimal("2550.00"),  // contado
                "Gerente"
        );

        assertEquals(0, count.getExpectedCash().compareTo(new BigDecimal("2550.00")),
                "El esperado debe ser fondo + ventas + ingresos - egresos");
    }

    @Test
    void laDiferenciaEsCeroCuandoLoContadoIgualaLoEsperado() {
        CashCount count = CashCount.generate(
                1L,
                new BigDecimal("1000.00"),
                new BigDecimal("500.00"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("1500.00"),  // contado = esperado (1000 + 500)
                "Gerente"
        );

        assertEquals(0, count.getDifference().compareTo(BigDecimal.ZERO),
                "Si lo contado iguala lo esperado, la diferencia es cero");
    }

    @Test
    void laDiferenciaEsNegativaCuandoFaltaEfectivo() {
        // esperado 1500, contado 1400 -> faltan 100
        CashCount count = CashCount.generate(
                1L,
                new BigDecimal("1000.00"),
                new BigDecimal("500.00"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("1400.00"),
                "Gerente"
        );

        assertEquals(0, count.getDifference().compareTo(new BigDecimal("-100.00")),
                "Un faltante debe dar diferencia negativa");
    }

    @Test
    void laDiferenciaEsPositivaCuandoSobraEfectivo() {
        // esperado 1500, contado 1600 -> sobran 100
        CashCount count = CashCount.generate(
                1L,
                new BigDecimal("1000.00"),
                new BigDecimal("500.00"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("1600.00"),
                "Gerente"
        );

        assertEquals(0, count.getDifference().compareTo(new BigDecimal("100.00")),
                "Un sobrante debe dar diferencia positiva");
    }

    @Test
    void losEgresosReducenElEsperado() {
        // fondo 1000, sin ventas ni ingresos, egreso 300 -> esperado 700
        CashCount count = CashCount.generate(
                1L,
                new BigDecimal("1000.00"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("300.00"),
                new BigDecimal("700.00"),
                "Gerente"
        );

        assertEquals(0, count.getExpectedCash().compareTo(new BigDecimal("700.00")));
        assertEquals(0, count.getDifference().compareTo(BigDecimal.ZERO));
    }

    @Test
    void noSePuedeGenerarUnCorteSinCaja() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CashCount.generate(
                        null,
                        new BigDecimal("1000.00"),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        new BigDecimal("1000.00"),
                        "Gerente"),
                "Un corte sin sesión de caja debe lanzar excepción"
        );
    }

    @Test
    void noSePuedeGenerarUnCorteConContadoNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CashCount.generate(
                        1L,
                        new BigDecimal("1000.00"),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        new BigDecimal("-50.00"),
                        "Gerente"),
                "Un corte con efectivo contado negativo debe lanzar excepción"
        );
    }

    @Test
    void noSePuedeGenerarUnCorteSinResponsable() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CashCount.generate(
                        1L,
                        new BigDecimal("1000.00"),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        new BigDecimal("1000.00"),
                        "  "),
                "Un corte sin responsable de aprobación debe lanzar excepción"
        );
    }
}