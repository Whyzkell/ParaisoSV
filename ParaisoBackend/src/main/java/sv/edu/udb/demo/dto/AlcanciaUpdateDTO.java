package sv.edu.udb.demo.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record AlcanciaUpdateDTO(
        String descr,
        @Positive BigDecimal precioMeta
) {}
