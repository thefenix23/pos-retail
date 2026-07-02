package com.postretail.backend.cashsession.application.usecase;

import com.postretail.backend.cashsession.domain.exception.CashSessionNotOpenException;
import com.postretail.backend.cashsession.domain.model.CashMovement;
import com.postretail.backend.cashsession.domain.model.CashSession;
import com.postretail.backend.cashsession.domain.model.CashStatus;
import com.postretail.backend.cashsession.domain.model.MovementType;
import com.postretail.backend.cashsession.domain.port.in.RegisterCashMovementUseCase;
import com.postretail.backend.cashsession.domain.port.out.CashMovementRepository;
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
 * Pruebas del caso de uso de registro de movimiento de efectivo.
 * Regla clave: solo se puede registrar en una caja abierta.
 */
public class RegisterCashMovementServiceTest {

    private static class SessionRepoFalso implements CashSessionRepository {
        private final CashSession session;
        SessionRepoFalso(CashSession session) { this.session = session; }
        @Override public boolean existsOpenSession(Long b, Long r) { return false; }
        @Override public Optional<CashSession> findOpenSession(Long b, Long r) { return Optional.empty(); }
        @Override public Optional<CashSession> findById(Long id) { return Optional.ofNullable(session); }
        @Override public CashSession save(CashSession s) { return s; }
        @Override public CashSession update(CashSession s) { return s; }
    }

    private static class MovementRepoFalso implements CashMovementRepository {
        boolean guardado = false;
        @Override public CashMovement save(CashMovement m) { guardado = true; return m; }
        @Override public List<CashMovement> findBySessionId(Long id) { return List.of(); }
        @Override public BigDecimal totalByType(Long id, String type) { return BigDecimal.ZERO; }
    }

    private CashSession sesion(CashStatus estado) {
        return new CashSession(
                1L, 1L, 1L, "Edi",
                new BigDecimal("1000.00"),
                estado,
                LocalDateTime.now(),
                estado == CashStatus.CLOSED ? LocalDateTime.now() : null
        );
    }

    private RegisterCashMovementUseCase.RegisterCashMovementCommand comando() {
        return new RegisterCashMovementUseCase.RegisterCashMovementCommand(
                1L, MovementType.EXPENSE, "Retiro para cambio", new BigDecimal("100.00"), "Edi");
    }

    @Test
    void registraElMovimientoEnUnaCajaAbierta() {
        SessionRepoFalso sessionRepo = new SessionRepoFalso(sesion(CashStatus.OPEN));
        MovementRepoFalso movementRepo = new MovementRepoFalso();

        RegisterCashMovementService service =
                new RegisterCashMovementService(sessionRepo, movementRepo);

        CashMovement movimiento = service.register(comando());

        assertTrue(movementRepo.guardado, "El movimiento debe haberse guardado");
        assertEquals(MovementType.EXPENSE, movimiento.getType());
        assertEquals(0, movimiento.getAmount().compareTo(new BigDecimal("100.00")));
    }

    @Test
    void noRegistraEnUnaCajaCerrada() {
        SessionRepoFalso sessionRepo = new SessionRepoFalso(sesion(CashStatus.CLOSED));
        RegisterCashMovementService service =
                new RegisterCashMovementService(sessionRepo, new MovementRepoFalso());

        assertThrows(
                CashSessionNotOpenException.class,
                () -> service.register(comando()),
                "No se puede registrar un movimiento en una caja cerrada"
        );
    }

    @Test
    void noRegistraSiLaCajaNoExiste() {
        SessionRepoFalso sessionRepo = new SessionRepoFalso(null);
        RegisterCashMovementService service =
                new RegisterCashMovementService(sessionRepo, new MovementRepoFalso());

        assertThrows(
                CashSessionNotOpenException.class,
                () -> service.register(comando()),
                "No se puede registrar un movimiento en una caja inexistente"
        );
    }
}