package co.edu.usbcali.autosusbcali.repository;

import co.edu.usbcali.autosusbcali.domain.RolPermiso;
import co.edu.usbcali.autosusbcali.domain.RolPermisoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {
    // Cuántos permisos tiene asignados el rol (se usa para el log de auditoría).
    @Query("select count(rp) from RolPermiso rp where rp.rol.id = :rolId")
    long contarPermisosPorRol(@Param("rolId") Long rolId);
}
