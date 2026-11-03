package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Rol;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarRolRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
import co.edu.usbcali.autosusbcali.mapper.RolMapper;
import co.edu.usbcali.autosusbcali.repository.RolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class RolService implements IRolService {
    private final RolRepository rolRepository;

    @Override
    public List<ObtenerRolResponse> obtenerTodos() {
        return RolMapper.ListaRolesAListaObtenerRolResponse(rolRepository.findAll());
    }

    @Override
    public List<ObtenerRolResponse> buscarRolPorEstado(Boolean activo) {
        List<Rol> rolesPorEstado = rolRepository.findByActivo(activo);
        List<ObtenerRolResponse> rolesResponse = RolMapper.ListaRolesAListaObtenerRolResponse(rolesPorEstado);
        return rolesResponse;
    }

    @Override
    public ObtenerRolResponse obtenerRolPorNombre(String nombre) {
        Rol rolPorNombre = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el rol por nombre: "+nombre));
        return RolMapper.rolAObtenerRolResponse(rolPorNombre);
    }

    @Override
    public ObtenerRolResponse obtenerRolPorId(Long id) {
        Rol rolPorId = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el rol por id "+id));
        return RolMapper.rolAObtenerRolResponse(rolPorId);
    }

    @Override
    public ObtenerRolResponse crearRol(CrearModificarRolRequest crearRolRequest) throws Exception {
        // Validaciones del objeto #crearRolRequest
        if (crearRolRequest == null){
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (crearRolRequest.nombre() == null
            || crearRolRequest.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacio.");
        }
        if (crearRolRequest.descripcion() == null
            || crearRolRequest.descripcion().isBlank()) {
            throw new Exception("La descripcion no puede ser nulo ni estar vacio.");
        }

        // Buscar si existe un rol con el nombre que se quiere agregar
        if (rolRepository.existsByNombre(crearRolRequest.nombre())) {
            throw new Exception("Ya existe un rol con el nombre " +crearRolRequest.nombre());
        }

        // Usar mapper para convertir DTO a objeto de Domain
        Rol rol = RolMapper.crearRolRequestARol(crearRolRequest);

        // Persistir el Rol en base de datos
        rol = rolRepository.save(rol);

        // Crear DTO response obtenerRolResponse
        ObtenerRolResponse rolResponse = RolMapper.rolAObtenerRolResponse(rol);

        // Retornar rolResponse
        return rolResponse;

    }

    @Override
    public ObtenerRolResponse modificarRol(Long id, CrearModificarRolRequest modificarRolRequest) throws Exception {
        // Validaciones del objeto #modificarRolRequest
        if (modificarRolRequest == null){
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (modificarRolRequest.nombre() == null
                || modificarRolRequest.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacio.");
        }
        if (modificarRolRequest.descripcion() == null
                || modificarRolRequest.descripcion().isBlank()) {
            throw new Exception("La descripcion no puede ser nulo ni estar vacio.");
        }

        // Buscar el rol que se quiere modificar por id
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new Exception("No se ha encontrado el rol por id " + id));

        // Si cambia el nombre, buscar si ya existe otro rol con ese nombre
        if (!rol.getNombre().equals(modificarRolRequest.nombre())
                && rolRepository.existsByNombre(modificarRolRequest.nombre())) {
            throw new Exception("Ya existe un rol con el nombre " + modificarRolRequest.nombre());
        }

        // Actualizar los datos del objeto de Domain con los del DTO
        rol.setNombre(modificarRolRequest.nombre());
        rol.setDescripcion(modificarRolRequest.descripcion());

        // Persistir el Rol en base de datos
        rol = rolRepository.save(rol);

        // Crear DTO response obtenerRolResponse
        ObtenerRolResponse rolResponse = RolMapper.rolAObtenerRolResponse(rol);

        // Retornar rolResponse
        return rolResponse;
    }

    @Override
    public void eliminarRol(Long id) throws Exception {
        // Buscar el rol que se quiere eliminar por id
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new Exception("No se ha encontrado el rol por id " + id));

        // Validar que el rol no este inactivo
        if (!rol.getActivo()) {
            throw new Exception("El rol con id " + id + " ya se encuentra inactivo.");
        }

        // Eliminacion logica: cambiar el estado del rol a inactivo
        rol.setActivo(false);

        // Persistir el cambio en base de datos
        rolRepository.save(rol);
    }

}

