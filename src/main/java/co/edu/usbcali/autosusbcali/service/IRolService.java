package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarRolRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;

import java.util.List;

public interface IRolService {
    // Buscamos todos los roles sin importar su estado
    List<ObtenerRolResponse> obtenerTodos();

    // Buscamos los roles por estado (ya sea activo o inactivo)
    List<ObtenerRolResponse> buscarRolPorEstado(Boolean activo);

    ObtenerRolResponse obtenerRolPorNombre(String nombre);

    ObtenerRolResponse obtenerRolPorId(Long id);

    // Metodo para crear un rol
    ObtenerRolResponse crearRol(CrearModificarRolRequest crearRolRequest) throws Exception;

    //Metodo para modificar un rol
    ObtenerRolResponse modificarRol(Long id, CrearModificarRolRequest modificarRolRequest) throws Exception;

    // Metodo para eliminar LOGICAMENTE un rol (la fila se conserva, solo se marca activo = false).
    // Lanza RolNoEncontradoException (404) si no existe y RolYaInactivoException (409) si ya estaba inactivo.
    void eliminarRolLogico(Long id);

    // Metodo para eliminar FISICAMENTE un rol (DELETE real en la base de datos).
    // Lanza RolNoEncontradoException (404) si no existe y RolEnUsoException (409) si tiene usuarios asignados.
    void eliminarRol(Long id);
}
