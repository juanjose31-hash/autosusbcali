package co.edu.usbcali.autosusbcali.exception;

/**
 * Se lanza cuando se intenta eliminar un rol que todavía está asignado a usuarios.
 * El GlobalExceptionHandler la traduce a HTTP 409 (Conflict).
 */
public class RolEnUsoException extends RuntimeException{
    public RolEnUsoException(String mensaje) {
        super(mensaje);
    }
}
