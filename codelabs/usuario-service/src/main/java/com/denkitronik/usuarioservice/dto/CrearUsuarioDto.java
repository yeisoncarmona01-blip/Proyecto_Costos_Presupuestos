package com.denkitronik.usuarioservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CrearUsuarioDto(
    @NotBlank String username,
    @NotBlank @Email String email,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String password,
    @NotBlank @Pattern(regexp = "USER|ADMIN") String rol
) {}
