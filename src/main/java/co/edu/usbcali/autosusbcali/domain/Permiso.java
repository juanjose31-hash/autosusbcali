package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permisos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modulo", length = 50, nullable = false)
    private String modulo;

    @Column(name = "accion", length = 30, nullable = false)
    private String accion;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;
}
