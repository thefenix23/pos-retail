package com.postretail.backend.shared.web;

import java.time.LocalDateTime;

/**
 * Cuerpo uniforme de respuesta de error para toda la API.
 * timestamp: cuándo ocurrió.
 * status: código HTTP.
 * error: nombre corto de estado HTTP.
 * message: mensaje legible para el cliente.
 * path: endpoint donde ocurrió
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}
