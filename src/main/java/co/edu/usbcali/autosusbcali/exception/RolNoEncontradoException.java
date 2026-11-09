package co.edu.usbcali.autosusbcali.exception;

/**
 * Se lanza cuando se busca un rol por id o nombre y no existe.
 * Es una excepción no verificada (RuntimeException) para no obligar a
 * declarar "throws" en cada método; el GlobalExceptionHandler la traduce a HTTP 404.
 */
public class RolNoEncontradoException extends RuntimeException{
    public RolNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public static RolNoEncontradoException porId(Long id) {
        return new RolNoEncontradoException("No se ha encontrado el rol por id " + id);
    }

    public static RolNoEncontradoException porNombre(String nombre) {
        return new RolNoEncontradoException("No se ha encontrado el rol por nombre: " + nombre);
    }
}
