package sv.edu.udb.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PerroEnAdopcion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerroEnAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Raza")
    private String raza;

    @Column(name = "Edad")
    private Integer edad;

    @Column(name = "Img")
    private String img;

    @Column(name = "Descr")
    private String descr;

    @CreationTimestamp
    @Column(name = "CreadoEn", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
}