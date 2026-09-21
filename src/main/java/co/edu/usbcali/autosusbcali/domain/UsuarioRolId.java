package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data

public class UsuarioRolId implements Serializable {
    private Long usuarioId;

    private Long rolId;
}
