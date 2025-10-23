package sv.edu.udb.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="Donaciones", schema="dbo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Donaciones {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IdUsuario", nullable=false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IdAlcancia", nullable=false)
    private Alcancia alcancia;

    @Column(name="CantidadDonada", nullable=false, precision=12, scale=2)
    private BigDecimal cantidadDonada;

    @Column(name="Fecha", insertable=false, updatable=false)
    private LocalDateTime fecha;
}
