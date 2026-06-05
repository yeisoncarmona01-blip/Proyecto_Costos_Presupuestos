package com.denkitronik.usuarioservice.dto;

import java.util.List;

/**
 * Respuesta paginada del listado de usuarios.
 * El frontend usa `total` para calcular cuántas páginas mostrar.
 */
public record PaginaDto(
    List<UsuarioDto> usuarios,
    int total,
    int page,
    int size
) {}
