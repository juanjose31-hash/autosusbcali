package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Rol;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarRolRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
import co.edu.usbcali.autosusbcali.exception.RolEnUsoException;
import co.edu.usbcali.autosusbcali.exception.RolNoEncontradoException;
import co.edu.usbcali.autosusbcali.exception.RolYaInactivoException;
import co.edu.usbcali.autosusbcali.mapper.RolMapper;
import co.edu.usbcali.autosusbcali.repository.RolPermisoRepository;
import co.edu.usbcali.autosusbcali.repository.RolRepository;
import co.edu.usbcali.autosusbcali.repository.UsuarioRolRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor

public class RolService implements IRolService {
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolPermisoRepository rolPermisoRepository;

    @Override
    public List<ObtenerRolResponse> obtenerTodos() {
        log.debug("Consultando todos los roles");
        List<ObtenerRolResponse> roles = RolMapper.ListaRolesAListaObtenerRolResponse(rolRepository.findAll());
        log.debug("Se encontraron {} roles", roles.size());
        return roles;
    }

    @Override
    public List<ObtenerRolResponse> buscarRolPorEstado(Boolean activo) {
        log.debug("Consultando roles con activo={}", activo);
        List<Rol> rolesPorEstado = rolRepository.findByActivo(activo);
        List<ObtenerRolResponse> rolesResponse = RolMapper.ListaRolesAListaObtenerRolResponse(rolesPorEstado);
        log.debug("Se encontraron {} roles con activo={}", rolesResponse.size(), activo);
        return rolesResponse;
    }

    @Override
    public ObtenerRolResponse obtenerRolPorNombre(String nombre) {
        log.debug("Buscando rol por nombre={}", nombre);
        Rol rolPorNombre = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> RolNoEncontradoException.porNombre(nombre));
        return RolMapper.rolAObtenerRolResponse(rolPorNombre);
    }

    @Override
    public ObtenerRolResponse obtenerRolPorId(Long id) {
        log.debug("Buscando rol por id={}", id);
        Rol rolPorId = rolRepository.findById(id)
                .orElseThrow(() -> RolNoEncontradoException.porId(id));
        return RolMapper.rolAObtenerRolResponse(rolPorId);
    }

    @Override
    public ObtenerRolResponse crearRol(CrearModificarRolRequest crearRolRequest) throws Exception {
        log.info("Iniciando creación de rol: {}", crearRolRequest);

        // Validaciones del objeto #crearRolRequest
        if (crearRolRequest == null){
            log.warn("Creación de rol rechazada: el request es nulo");
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (crearRolRequest.nombre() == null
                || crearRolRequest.nombre().isBlank()) {
            log.warn("Creación de rol rechazada: nombre nulo o vacío");
            throw new Exception("El nombre no puede ser nulo ni estar vacio.");
        }
        if (crearRolRequest.descripcion() == null
                || crearRolRequest.descripcion().isBlank()) {
            log.warn("Creación de rol rechazada: descripción nula o vacía");
            throw new Exception("La descripcion no puede ser nulo ni estar vacio.");
        }

        // Buscar si existe un rol con el nombre que se quiere agregar
        if (rolRepository.existsByNombre(crearRolRequest.nombre())) {
            log.warn("Creación de rol rechazada: ya existe el rol con nombre={}", crearRolRequest.nombre());
            throw new Exception("Ya existe un rol con el nombre " +crearRolRequest.nombre());
        }

        // Usar mapper para convertir DTO a objeto de Domain
        Rol rol = RolMapper.crearRolRequestARol(crearRolRequest);

        // Persistir el Rol en base de datos
        rol = rolRepository.save(rol);

        // Crear DTO response obtenerRolResponse
        ObtenerRolResponse rolResponse = RolMapper.rolAObtenerRolResponse(rol);

        log.info("Rol creado correctamente: id={}, nombre={}", rol.getId(), rol.getNombre());

        // Retornar rolResponse
        return rolResponse;

    }

    @Override
    public ObtenerRolResponse modificarRol(Long id, CrearModificarRolRequest modificarRolRequest) throws Exception {
        log.info("Iniciando modificación del rol id={} con datos: {}", id, modificarRolRequest);

        // Validaciones del objeto #modificarRolRequest
        if (modificarRolRequest == null){
            log.warn("Modificación del rol id={} rechazada: el request es nulo", id);
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (modificarRolRequest.nombre() == null
                || modificarRolRequest.nombre().isBlank()) {
            log.warn("Modificación del rol id={} rechazada: nombre nulo o vacío", id);
            throw new Exception("El nombre no puede ser nulo ni estar vacio.");
        }
        if (modificarRolRequest.descripcion() == null
                || modificarRolRequest.descripcion().isBlank()) {
            log.warn("Modificación del rol id={} rechazada: descripción nula o vacía", id);
            throw new Exception("La descripcion no puede ser nulo ni estar vacio.");
        }

        // Buscar el rol que se quiere modificar por id
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> RolNoEncontradoException.porId(id));

        // Si cambia el nombre, buscar si ya existe otro rol con ese nombre
        if (!rol.getNombre().equals(modificarRolRequest.nombre())
                && rolRepository.existsByNombre(modificarRolRequest.nombre())) {
            log.warn("Modificación del rol id={} rechazada: ya existe otro rol con nombre={}",
                    id, modificarRolRequest.nombre());
            throw new Exception("Ya existe un rol con el nombre " + modificarRolRequest.nombre());
        }

        // Actualizar los datos del objeto de Domain con los del DTO
        rol.setNombre(modificarRolRequest.nombre());
        rol.setDescripcion(modificarRolRequest.descripcion());

        // Persistir el Rol en base de datos
        rol = rolRepository.save(rol);

        // Crear DTO response obtenerRolResponse
        ObtenerRolResponse rolResponse = RolMapper.rolAObtenerRolResponse(rol);

        log.info("Rol modificado correctamente: id={}, nombre={}", rol.getId(), rol.getNombre());

        // Retornar rolResponse
        return rolResponse;
    }

    /**
     * Eliminación LÓGICA: la fila NO se borra, solo se marca activo = false (UPDATE, no DELETE).
     * Los datos y sus relaciones (usuarios_roles, roles_permisos) se conservan intactos
     * y el rol se puede reactivar más adelante.
     */
    @Override
    @Transactional
    public void eliminarRolLogico(Long id) {
        log.info("Solicitud de eliminación lógica del rol id={}", id);

        // Buscar el rol que se quiere desactivar por id
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Eliminación lógica rechazada: no existe el rol con id={}", id);
                    return RolNoEncontradoException.porId(id);
                });

        // Validar que el rol no este inactivo
        if (!rol.getActivo()) {
            log.warn("Eliminación lógica rechazada: el rol id={} ya se encuentra inactivo", id);
            throw new RolYaInactivoException("El rol con id " + id + " ya se encuentra inactivo.");
        }

        // Eliminacion logica: cambiar el estado del rol a inactivo
        rol.setActivo(false);

        // Persistir el cambio en base de datos
        rolRepository.save(rol);

        log.info("AUDITORIA: rol desactivado lógicamente id={}, nombre={}", id, rol.getNombre());
    }

    /**
     * Eliminación FÍSICA: la fila desaparece de la tabla "roles".
     *
     * @Transactional: si algo falla a mitad de camino (por ejemplo, un error al ejecutar el DELETE),
     * se hace rollback y la base de datos queda exactamente como estaba antes del método.
     * Las RuntimeException (como RolEnUsoException) provocan rollback automático.
     */
    @Override
    @Transactional
    public void eliminarRol(Long id) {
        log.info("Solicitud de eliminación física del rol id={}", id);

        // 1. Verificar que el rol exista (si no, 404 vía GlobalExceptionHandler)
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Eliminación rechazada: no existe el rol con id={}", id);
                    return RolNoEncontradoException.porId(id);
                });

        // 2. Regla de negocio: no borrar un rol asignado a usuarios.
        // En la BD, usuarios_roles tiene ON DELETE CASCADE hacia roles: si no validamos aquí,
        // el DELETE borraría en silencio las asignaciones y esos usuarios quedarían sin rol.
        if (usuarioRolRepository.existeUsuarioConRol(id)) {
            log.warn("Eliminación rechazada: el rol id={} ('{}') está asignado a usuarios", id, rol.getNombre());
            throw new RolEnUsoException("No se puede eliminar el rol '" + rol.getNombre()
                    + "' porque está asignado a uno o más usuarios.");
        }

        // 3. Los permisos del rol (roles_permisos) pertenecen al propio rol: la BD los elimina en cascada.
        // Solo los contamos para dejarlos en el log de auditoría.
        long permisosDesvinculados = rolPermisoRepository.contarPermisosPorRol(id);

        // 4. Eliminación física. flush() fuerza el DELETE ahora, dentro del método,
        // para que cualquier error de BD ocurra ANTES de escribir el log de éxito.
        rolRepository.delete(rol);
        rolRepository.flush();

        // 5. Log de auditoría
        log.info("AUDITORIA: rol eliminado físicamente id={}, nombre={}, permisos desvinculados={}",
                id, rol.getNombre(), permisosDesvinculados);
    }


}

