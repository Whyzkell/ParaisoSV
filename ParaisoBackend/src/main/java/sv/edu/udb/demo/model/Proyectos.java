package sv.edu.udb.demo.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proyectos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Tit", nullable = false)
    private String tit;

    @Column(name = "Descr")
    private String descr;

    @Column(name = "Img")
    private String img;

    @CreationTimestamp
    @Column(name = "CreadoEn", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
}