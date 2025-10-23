package sv.edu.udb.demo.dto;

import jakarta.validation.constraints.Min;

public record PerroUpdateDTO(
        String nombre,
        String raza,
        @Min(0) Integer edad,
        String img,
        String descr
) { }
