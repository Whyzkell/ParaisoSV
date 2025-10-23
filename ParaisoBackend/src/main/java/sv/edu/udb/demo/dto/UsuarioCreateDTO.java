package sv.edu.udb.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCreateDTO(
        @NotBlank String nombre,
        @NotBlank @Email String correo,
        @NotBlank String password,   // se guardará con BCrypt
        String rol                   // opcional; si viene null/blank => "USER"
) { }