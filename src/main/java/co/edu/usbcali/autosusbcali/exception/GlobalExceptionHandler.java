package co.edu.usbcali.autosusbcali.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejo centralizado de excepciones para todos los controladores.
 * Usa ProblemDetail (RFC 9457), el formato de error estándar de Spring.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 404: el recurso no existe. Es un caso "esperado" del cliente -> WARN, sin stacktrace.
    @ExceptionHandler(RolNoEncontradoException.class)
    public ProblemDetail manejarRolNoEncontrado(RolNoEncontradoException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409: el rol está en uso y no se puede eliminar.
    @ExceptionHandler(RolEnUsoException.class)
    public ProblemDetail manejarRolEnUso(RolEnUsoException ex) {
        log.warn("Conflicto de negocio: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 409: se intentó desactivar un rol que ya estaba inactivo.
    @ExceptionHandler(RolYaInactivoException.class)
    public ProblemDetail manejarRolYaInactivo(RolYaInactivoException ex) {
        log.warn("Conflicto de negocio: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 409: red de seguridad si la base de datos rechaza la operación por una clave foránea.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail manejarIntegridad(DataIntegrityViolationException ex) {
        // La excepción va como ÚLTIMO parámetro para que Logback imprima el stacktrace.
        log.error("Violación de integridad de datos", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "La operación viola una restricción de integridad de la base de datos.");
    }

    // 500: cualquier otra excepción inesperada. Al cliente no se le exponen detalles internos.
    @ExceptionHandler(Exception.class)
    public ProblemDetail manejarInesperada(Exception ex) {
        log.error("Error inesperado", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor.");
    }
}
