package com.postretail.backend.sale.infrastructure.adapter;

import com.postretail.backend.cashsession.domain.port.out.CashSalesSummaryPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Punto de contacto entre cashsession y sale para el corte.
 * Implementa el puerto que cashsession definió (CashSalesSummaryPort)
 * y por dentro consulta las tablas de venta.
 */
@Component
public class CashSalesSummaryAdapter implements CashSalesSummaryPort {

    private final CashSalesQueryRepository queryRepository;

    public CashSalesSummaryAdapter(CashSalesQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public BigDecimal cashTotalForSession(Long cashSessionId) {
        BigDecimal total = queryRepository.cashTotalForSession(cashSessionId);

        return total != null ? total : BigDecimal.ZERO;
    }
}
