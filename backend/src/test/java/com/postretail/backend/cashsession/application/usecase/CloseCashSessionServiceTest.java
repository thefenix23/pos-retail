package com.postretail.backend.cashsession.application.usecase;

import com.postretail.backend.cashsession.domain.exception.CashSessionNotOpenException;
import com.postretail.backend.cashsession.domain.model.CashCount;
import com.postretail.backend.cashsession.domain.model.CashMovement;
import com.postretail.backend.cashsession.domain.model.CashSession;
import com.postretail.backend.cashsession.domain.model.CashStatus;
import com.postretail.backend.cashsession.domain.model.MovementType;
import com.postretail.backend.cashsession.domain.port.in.CloseCashSessionUseCase;
import com.postretail.backend.cashsession.domain.port.out.CashCountRepository;
import com.postretail.backend.cashsession.domain.port.out.CashMovementRepository;
import com.postretail.backend.cashsession.domain.port.out.CashSalesSummaryPort;
import com.postretail.backend.cashsession.domain.port.out.CashSessionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas del caso de uso de cierre de caja.
 * Usa dobles en memoria (sin Spring ni base de datos) para verificar:
 * - que reúne los totales de los puertos y calcula el corte correcto;
 * - que cierra la sesión;
 * - que rechaza cerrar una caja que no está abierta.
 */
public class CloseCashSessionServiceTest {

    // --- Dobles en memoria de los puertos ---

    private static class SessionRepoFalso implements CashSessionRepository {
        CashSession session;
        boolean actualizada = false;

        SessionRepoFalso(CashSession session) {
            this.session = session;
        }

        @Override public boolean existsOpenSession(Long b, Long r) { return false; }
        @Override public Optional<CashSession> findOpenSession(Long b, Long r) { return Optional.empty(); }
        @Override public Optional<CashSession> findById(Long id) { return Optional.ofNullable(session); }
        @Override public CashSession save(CashSession s) { return s; }
        @Override public CashSession update(CashSession s) {
            this.session = s;
            this.actualizada = true;
            return s;
        }
    }

    private static class MovementRepoFalso implements CashMovementRepository {
        private final BigDecimal incomes;
        private final BigDecimal expenses;

        MovementRepoFalso(BigDecimal incomes, BigDecimal expenses) {
            this.incomes = incomes;
            this.expenses = expenses;
        }

        @Override public CashMovement save(CashMovement m) { return m; }
        @Override public List<CashMovement> findBySessionId(Long id) { return List.of(); }
        @Override public BigDecimal totalByType(Long sessionId, String type) {
            return type.equals(MovementType.INCOME.name()) ? incomes : expenses;
        }
    }

    private static class CountRepoFalso implements CashCountRepository {
        CashCount guardado;
        @Override public CashCount save(CashCount c) { this.guardado = c; return c; }
        @Override public Optional<CashCount> findBySessionId(Long id) { return Optional.ofNullable(guardado); }
    }

    private CashSession sesionAbierta() {
        return new CashSession(
                1L, 1L, 1L, "Edi",
                new BigDecimal("1500.00"),
                CashStatus.OPEN,
                LocalDateTime.now(),
                null
        );
    }

    // --- Pruebas ---

    @Test
    void cierraLaCajaYGeneraElCorteConLosTotalesCorrectos() {
        SessionRepoFalso sessionRepo = new SessionRepoFalso(sesionAbierta());
        MovementRepoFalso movementRepo = new MovementRepoFalso(
                new BigDecimal("1000.00"), new BigDecimal("100.00"));
        CountRepoFalso countRepo = new CountRepoFalso();
        CashSalesSummaryPort salesPort = sessionId -> new BigDecimal("150.00");

        CloseCashSessionService service = new CloseCashSessionService(
                sessionRepo, movementRepo, countRepo, salesPort);

        // esperado = 1500 + 150 + 1000 - 100 = 2550
        CashCount corte = service.close(
                new CloseCashSessionUseCase.CloseCashSessionCommand(
                        1L, new BigDecimal("2550.00"), "Gerente"));

        assertEquals(0, corte.getExpectedCash().compareTo(new BigDecimal("2550.00")));
        assertEquals(0, corte.getDifference().compareTo(BigDecimal.ZERO));
        assertTrue(sessionRepo.actualizada, "La sesión debe haberse actualizado (cerrada)");
        assertEquals(CashStatus.CLOSED, sessionRepo.session.getStatus());
    }

    @Test
    void detectaFaltanteEnElCorte() {
        SessionRepoFalso sessionRepo = new SessionRepoFalso(sesionAbierta());
        MovementRepoFalso movementRepo = new MovementRepoFalso(BigDecimal.ZERO, BigDecimal.ZERO);
        CountRepoFalso countRepo = new CountRepoFalso();
        CashSalesSummaryPort salesPort = sessionId -> BigDecimal.ZERO;

        CloseCashSessionService service = new CloseCashSessionService(
                sessionRepo, movementRepo, countRepo, salesPort);

        // esperado = 1500, contado 1400 -> faltan 100
        CashCount corte = service.close(
                new CloseCashSessionUseCase.CloseCashSessionCommand(
                        1L, new BigDecimal("1400.00"), "Gerente"));

        assertEquals(0, corte.getDifference().compareTo(new BigDecimal("-100.00")));
    }

    @Test
    void noCierraSiLaCajaNoExiste() {
        SessionRepoFalso sessionRepo = new SessionRepoFalso(null); // findById devuelve vacío
        CloseCashSessionService service = new CloseCashSessionService(
                sessionRepo,
                new MovementRepoFalso(BigDecimal.ZERO, BigDecimal.ZERO),
                new CountRepoFalso(),
                sessionId -> BigDecimal.ZERO);

        assertThrows(
                CashSessionNotOpenException.class,
                () -> service.close(new CloseCashSessionUseCase.CloseCashSessionCommand(
                        99L, new BigDecimal("100.00"), "Gerente")),
                "Cerrar una caja inexistente debe lanzar excepción"
        );
    }

    @Test
    void noCierraSiLaCajaYaEstaCerrada() {
        CashSession cerrada = new CashSession(
                1L, 1L, 1L, "Edi",
                new BigDecimal("1500.00"),
                CashStatus.CLOSED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        SessionRepoFalso sessionRepo = new SessionRepoFalso(cerrada);
        CloseCashSessionService service = new CloseCashSessionService(
                sessionRepo,
                new MovementRepoFalso(BigDecimal.ZERO, BigDecimal.ZERO),
                new CountRepoFalso(),
                sessionId -> BigDecimal.ZERO);

        assertThrows(
                CashSessionNotOpenException.class,
                () -> service.close(new CloseCashSessionUseCase.CloseCashSessionCommand(
                        1L, new BigDecimal("1500.00"), "Gerente")),
                "Cerrar una caja ya cerrada debe lanzar excepción"
        );
    }
}