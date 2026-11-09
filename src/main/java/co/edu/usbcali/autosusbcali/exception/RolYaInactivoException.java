package co.edu.usbcali.autosusbcali.exception;

/**
 * Se lanza cuando se intenta desactivar (eliminación lógica) un rol que ya está inactivo.
 * El GlobalExceptionHandler la traduce a HTTP 409 (Conflict).
 */
public class RolYaInactivoException extends RuntimeException{
    public RolYaInactivoException(String mensaje) {
        super(mensaje);
    }
}
