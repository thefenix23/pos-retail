package com.postretail.backend.cashsession.infrastructure.web;

import com.postretail.backend.cashsession.domain.model.CashCount;
import com.postretail.backend.cashsession.domain.model.CashMovement;
import com.postretail.backend.cashsession.domain.model.CashSession;
import com.postretail.backend.cashsession.domain.port.in.CloseCashSessionUseCase;
import com.postretail.backend.cashsession.domain.port.in.OpenCashSessionUseCase;
import com.postretail.backend.cashsession.domain.port.in.RegisterCashMovementUseCase;
import com.postretail.backend.cashsession.infrastructure.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador de entrada REST.
 * Depende del puerto OpenCashSessionUseCase, nunca de la implementación concreta.
 */
@RestController
@RequestMapping("/api/cash-sessions")
public class CashSessionController {

    private final OpenCashSessionUseCase openCashSessionUseCase;
    private final RegisterCashMovementUseCase registerCashMovementUseCase;
    private final CloseCashSessionUseCase closeCashSessionUseCase;

    public CashSessionController(OpenCashSessionUseCase openCashSessionUseCase, RegisterCashMovementUseCase registerCashMovementUseCase, CloseCashSessionUseCase closeCashSessionUseCase) {
        this.openCashSessionUseCase = openCashSessionUseCase;
        this.registerCashMovementUseCase = registerCashMovementUseCase;
        this.closeCashSessionUseCase = closeCashSessionUseCase;
    }

    // ------ Abrir caja ------
    @PostMapping
    public ResponseEntity<CashSessionResponse> open(@RequestBody OpenCashSessionRequest request) {
        CashSession session = openCashSessionUseCase.open(
                request.branchId(),
                request.registerId(),
                request.cashier(),
                request.initialFund()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CashSessionResponse.from(session));
    }

    // ------ Registrar movimiento de efectivo (ingreso/egreso) ------
    @PostMapping("/{id}/movements")
    public ResponseEntity<CashMovementResponse> registerMovement(@PathVariable Long id, @RequestBody RegisterCashMovementRequest request) {
        var command = new RegisterCashMovementUseCase.RegisterCashMovementCommand(
                id,
                request.type(),
                request.concept(),
                request.amount(),
                request.responsible()
        );

        CashMovement movement = registerCashMovementUseCase.register(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CashMovementResponse.from(movement));
    }

    // ------ Cerrar caja y generar corte ------
    @PostMapping("/{id}/close")
    public ResponseEntity<CashCountResponse> close(@PathVariable Long id, @RequestBody CloseCashSessionRequest request) {
        var command = new CloseCashSessionUseCase.CloseCashSessionCommand(
                id,
                request.countedCash(),
                request.approvedBy()
        );

        CashCount count = closeCashSessionUseCase.close(command);

        return ResponseEntity
                .ok(CashCountResponse.from(count));
    }
}
