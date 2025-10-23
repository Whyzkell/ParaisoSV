package sv.edu.udb.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PerroCreateDTO(
        @NotBlank String nombre,
        @NotBlank String raza,
        @Min(0) Integer edad,   // años (0+)
        String img,             // URL opcional
        String descr            // opcional
) { }
