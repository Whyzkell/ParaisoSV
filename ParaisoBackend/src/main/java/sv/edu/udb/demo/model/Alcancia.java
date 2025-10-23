package sv.edu.udb.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="Alcancia", schema="dbo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Alcancia {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Integer id;

    @Column(name="Descr", length=300)
    private String descr;

    @Column(name="PrecioMeta", nullable=false, precision=12, scale=2)
    private BigDecimal precioMeta;

    @Column(name="PrecioActual", nullable=false, precision=12, scale=2)
    private BigDecimal precioActual;

    @Column(name="CreadoEn", insertable=false, updatable=false)
    private LocalDateTime creadoEn;
}
