package sv.edu.udb.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeDTO(@NotBlank String password) { }
