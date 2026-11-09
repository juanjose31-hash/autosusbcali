package co.edu.usbcali.autosusbcali.repository;

import co.edu.usbcali.autosusbcali.domain.UsuarioRol;
import co.edu.usbcali.autosusbcali.domain.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
    // ¿Hay al menos un usuario con este rol asignado?
    @Query("select count(ur) > 0 from UsuarioRol ur where ur.rol.id = :rolId")
    boolean existeUsuarioConRol(@Param("rolId") Long rolId);
}
