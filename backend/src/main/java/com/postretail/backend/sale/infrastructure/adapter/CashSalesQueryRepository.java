package com.postretail.backend.sale.infrastructure.adapter;

import com.postretail.backend.sale.infrastructure.persistence.SalePaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * Repositorio de solo lectura para resumir ventas en efectivo por sesión de caja.
 * Vive en sale porque consulta entities de sale (SalePaymentJpaEntity).
 *
 * Usa JQPL sobre las entities, consiste con el resto del proyecto.
 */
public interface CashSalesQueryRepository extends JpaRepository<SalePaymentJpaEntity, Long> {
    /**
     * Suma de los pagos en efectivo de las ventas de una sesión de caja.
     * Recorre SalePaymentJpaEntity, navega a la venta (sp.sale.cashSessionId)
     * y cruza con el método de pagp "Cash" por su id.
     * COALESCE evita null cuando no hubo ventas en efectivo.
     *
     * El id del metodo "Cash" se resuelve por su nombre (viene del seed V4__seed_catalogs).
     */
    @Query("""
            SELECT COALESCE(SUM(sp.amount), 0)
            FROM SalePaymentJpaEntity sp
            WHERE sp.sale.cashSessionId = :sessionId
            AND sp.paymentMethodId = (
                SELECT pm.id
                FROM PaymentMethodJpaEntity pm
                WHERE pm.name = 'Cash'
            )
            """)
    BigDecimal cashTotalForSession(@Param("sessionId") Long sessionId);
}
