package com.postretail.backend.shared.web;

import com.postretail.backend.cashsession.domain.exception.CashSessionAlreadyOpenException;
import com.postretail.backend.cashsession.domain.exception.CashSessionNotOpenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Manejo centralizado de excepciones para toda la API.
 * Traduce excepciones del dominio y de la aplicación a códigos HTTP correctos,
 * con un cuerpo de error uniforme (ApiError).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Datos inválidos enviados por el cliente:
     * producto inexistente, fondo negativo, pagos que no cubren el total, etc.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Operación no permitida por el estado actual:
     * no hay caja abierta, producto inactivo, stock insuficiente.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Caja ya abierta para esa sucursal/caja. Excepción tipada del dominio.
     */
    @ExceptionHandler(CashSessionAlreadyOpenException.class)
    public ResponseEntity<ApiError> handleCashSessionAlreadyOpen(CashSessionAlreadyOpenException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Red de seguridad: cualquier excepción no contemplada es un 500 real.
     * No exponemos el mensaje interno al cliente para no filtrar detalles
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                request
        );
    }

    @ExceptionHandler(CashSessionNotOpenException.class)
    public ResponseEntity<ApiError> handleCashSessionNotOpen(CashSessionNotOpenException ex, HttpServletRequest request) {
        return build(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest request) {
        ApiError body = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }
}
