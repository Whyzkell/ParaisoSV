package sv.edu.udb.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Alcancia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alcancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Descr")
    private String descr;

    @Column(name = "PrecioMeta", nullable = false)
    private Double precioMeta;

    @Column(name = "PrecioActual", nullable = false)
    private Double precioActual = 0.0;

    @Column(name = "CreadoEn", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    @OneToMany(mappedBy = "alcancia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donaciones> donaciones;
}