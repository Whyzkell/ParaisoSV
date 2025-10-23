package sv.edu.udb.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record ProyectosCreateDTO(
        @NotBlank String tit,
        @NotBlank String descr,
        String img   // opcional
) { }
