package sv.edu.udb.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Donaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "IdUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "IdAlcancia", nullable = false)
    private Alcancia alcancia;

    @Column(name = "CantidadDonada", nullable = false)
    private Double cantidadDonada;

    @Column(name = "Fecha", nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}