package com.denkitronik.usuarioservice.dto;

import java.util.List;

public record UsuarioDto(
    String id,
    String username,
    String email,
    String firstName,
    String lastName,
    boolean enabled,
    List<String> roles,
    String fotoUrl    // URL de la foto de perfil. Puede ser null si el usuario no tiene foto.
) {}
