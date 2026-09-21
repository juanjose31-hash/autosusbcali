package co.edu.usbcali.autosusbcali.repository;

import co.edu.usbcali.autosusbcali.domain.BusquedaGuardada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusquedaGuardadaRepository extends JpaRepository<BusquedaGuardada, Long> {
    List<BusquedaGuardada> findByUsuarioId(Long usuarioId);
}
