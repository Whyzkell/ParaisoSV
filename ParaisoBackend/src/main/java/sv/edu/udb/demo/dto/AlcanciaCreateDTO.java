package sv.edu.udb.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record AlcanciaCreateDTO(
        String descr,
        @NotNull @Positive BigDecimal precioMeta
) {}
