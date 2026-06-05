package com.denkitronik.usuarioservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ActualizarUsuarioDto(
    @NotBlank @Email String email,
    @NotBlank String firstName,
    @NotBlank String lastName,
    boolean enabled,
    @NotBlank @Pattern(regexp = "USER|ADMIN") String rol
) {}
