package co.edu.usbcali.autosusbcali.repository;

import co.edu.usbcali.autosusbcali.domain.UsuarioRol;
import co.edu.usbcali.autosusbcali.domain.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
}
