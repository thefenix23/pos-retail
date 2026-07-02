package com.postretail.backend.cashsession.infrastructure.config;

import com.postretail.backend.cashsession.application.usecase.CloseCashSessionService;
import com.postretail.backend.cashsession.application.usecase.OpenCashSessionService;
import com.postretail.backend.cashsession.application.usecase.RegisterCashMovementService;
import com.postretail.backend.cashsession.domain.port.in.CloseCashSessionUseCase;
import com.postretail.backend.cashsession.domain.port.in.OpenCashSessionUseCase;
import com.postretail.backend.cashsession.domain.port.in.RegisterCashMovementUseCase;
import com.postretail.backend.cashsession.domain.port.out.CashCountRepository;
import com.postretail.backend.cashsession.domain.port.out.CashMovementRepository;
import com.postretail.backend.cashsession.domain.port.out.CashSalesSummaryPort;
import com.postretail.backend.cashsession.domain.port.out.CashSessionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cableado del módulo
 * El adaptador (CashSessionRepositoryAdapter) entra por @Component y Spring lo inyecta aquí.
 * El caso de uso se registra como @Bean con new: el dominio queda libre se Spring
 */
@Configuration
public class CashSessionConfig {

    @Bean
    public OpenCashSessionUseCase openCashSessionUseCase(CashSessionRepository repository) {
        return new OpenCashSessionService(repository);
    }

    @Bean
    public RegisterCashMovementUseCase registerCashMovementUseCase(CashSessionRepository cashSessionRepository, CashMovementRepository cashMovementRepository) {
        return new RegisterCashMovementService(
                cashSessionRepository,
                cashMovementRepository
        );
    }

    @Bean
    public CloseCashSessionUseCase closeCashSessionUseCase(CashSessionRepository cashSessionRepository, CashMovementRepository cashMovementRepository, CashCountRepository cashCountRepository, CashSalesSummaryPort cashSalesSummaryPort) {
        return new CloseCashSessionService(
                cashSessionRepository,
                cashMovementRepository,
                cashCountRepository,
                cashSalesSummaryPort
        );
    }
}
