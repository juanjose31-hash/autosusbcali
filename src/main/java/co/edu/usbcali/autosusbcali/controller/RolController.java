package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarRolRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
import co.edu.usbcali.autosusbcali.service.IRolService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RolController {
    private final IRolService rolService;

    @GetMapping
    public List<ObtenerRolResponse> obtenerTodos() {
        return rolService.obtenerTodos();
    }

    @GetMapping("/activos")
    public List<ObtenerRolResponse> obtenerTodosActivos() {
        return rolService.buscarRolPorEstado(true);
    }

    @GetMapping("/inactivos")
    public List<ObtenerRolResponse> obtenerTodosInactivos() {
        return rolService.buscarRolPorEstado(false);
    }

    // Si el rol no existe, el servicio lanza RolNoEncontradoException y GlobalExceptionHandler responde 404.
    @GetMapping("/por-nombre/{nombre}")
    public ResponseEntity<ObtenerRolResponse> obtenerPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(rolService.obtenerRolPorNombre(nombre));
    }

    @GetMapping("/por-id/{id}")
    public ResponseEntity<ObtenerRolResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.obtenerRolPorId(id));
    }

    @PostMapping("/guardar")
    public ResponseEntity<ObtenerRolResponse> crearRol(
            @RequestBody CrearModificarRolRequest crearRolRequest) throws Exception {
        return new ResponseEntity<>(
                rolService.crearRol(crearRolRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ObtenerRolResponse> modificarRol(
            @PathVariable Long id,
            @RequestBody CrearModificarRolRequest crearRolRequest
    ) throws Exception {
        return new ResponseEntity<>(
                rolService.modificarRol(id, crearRolRequest),
                HttpStatus.OK
        );
    }

    // Eliminación LÓGICA: la fila se conserva con activo = false.
    // 204 No Content -> se desactivó | 404 -> no existe | 409 -> ya estaba inactivo
    @DeleteMapping("/eliminar-logico/{id}")
    public ResponseEntity<Void> eliminarRolLogico(@PathVariable Long id) {
        rolService.eliminarRolLogico(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminación física.
    // 204 No Content -> se eliminó | 404 -> no existe | 409 -> el rol está asignado a usuarios
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }


    }
