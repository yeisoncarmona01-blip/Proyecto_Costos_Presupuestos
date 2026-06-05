package com.denkitronik.usuarioservice.controllers;

import com.denkitronik.usuarioservice.dto.ActualizarUsuarioDto;
import com.denkitronik.usuarioservice.dto.CrearUsuarioDto;
import com.denkitronik.usuarioservice.dto.PaginaDto;
import com.denkitronik.usuarioservice.dto.UsuarioDto;
import com.denkitronik.usuarioservice.service.MinioService;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/${api.version}/usuario-service")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioRestController {

    private final RealmResource realmResource;
    private final MinioService  minioService;

    public UsuarioRestController(RealmResource realmResource, MinioService minioService) {
        this.realmResource = realmResource;
        this.minioService  = minioService;
    }

    @GetMapping("/usuarios")
    public PaginaDto listar(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        int first = page * size;
        List<UsuarioDto> usuarios = realmResource.users().list(first, size)
                .stream().map(this::toDto).toList();
        int total = realmResource.users().count();
        return new PaginaDto(usuarios, total, page, size);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> obtener(@PathVariable String id) {
        try {
            UserRepresentation user = realmResource.users().get(id).toRepresentation();
            return ResponseEntity.ok(toDto(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Usuario no encontrado: " + id));
        }
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> crear(@Valid @RequestBody CrearUsuarioDto dto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        try (Response response = realmResource.users().create(user)) {
            if (response.getStatus() == 409) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("mensaje", "El username ya existe"));
            }
            if (response.getStatus() != 201) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("mensaje", "Error al crear usuario: " + response.getStatus()));
            }
            String location = response.getHeaderString("Location");
            String userId   = location.substring(location.lastIndexOf('/') + 1);

            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(dto.password());
            cred.setTemporary(false);
            realmResource.users().get(userId).resetPassword(cred);

            asignarRol(userId, dto.rol());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "Usuario creado", "id", userId));
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id,
                                        @Valid @RequestBody ActualizarUsuarioDto dto) {
        try {
            UserResource userResource = realmResource.users().get(id);
            UserRepresentation user   = userResource.toRepresentation();

            user.setEmail(dto.email());
            user.setFirstName(dto.firstName());
            user.setLastName(dto.lastName());
            user.setEnabled(dto.enabled());
            userResource.update(user);

            String rolActual = obtenerRolPrincipal(id);
            if (!rolActual.equals(dto.rol())) {
                if (!rolActual.isEmpty()) {
                    RoleRepresentation rolAnterior = realmResource.roles().get(rolActual).toRepresentation();
                    userResource.roles().realmLevel().remove(List.of(rolAnterior));
                }
                asignarRol(id, dto.rol());
            }

            return ResponseEntity.ok(Map.of("mensaje", "Usuario actualizado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Usuario no encontrado: " + id));
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        try {
            realmResource.users().get(id).remove();
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Usuario no encontrado: " + id));
        }
    }

    /**
     * Recibe una imagen (multipart/form-data), la sube a MinIO y guarda
     * la URL pública como atributo "foto_url" del usuario en Keycloak.
     */
    @PostMapping(value = "/usuarios/{id}/foto",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirFoto(
            @PathVariable String id,
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            // 1. Subir imagen a MinIO → obtener URL pública
            String fotoUrl = minioService.subirFoto(id, archivo);

            // 2. Leer la representación actual del usuario en Keycloak
            UserResource       userResource = realmResource.users().get(id);
            UserRepresentation user         = userResource.toRepresentation();

            // 3. Agregar/actualizar el atributo foto_url
            //    Keycloak guarda atributos como Map<String, List<String>>
            Map<String, List<String>> atributos = user.getAttributes();
            if (atributos == null) {
                atributos = new HashMap<>();
            }
            atributos.put("foto_url", List.of(fotoUrl));
            user.setAttributes(atributos);

            // 4. Persistir el cambio en Keycloak
            userResource.update(user);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Foto actualizada",
                    "fotoUrl", fotoUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al subir la foto: " + e.getMessage()));
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void asignarRol(String userId, String rolNombre) {
        RoleRepresentation rol = realmResource.roles().get(rolNombre).toRepresentation();
        realmResource.users().get(userId).roles().realmLevel()
                .add(Collections.singletonList(rol));
    }

    private String obtenerRolPrincipal(String userId) {
        return realmResource.users().get(userId).roles().realmLevel().listEffective()
                .stream()
                .map(RoleRepresentation::getName)
                .filter(r -> r.equals("USER") || r.equals("ADMIN"))
                .findFirst()
                .orElse("");
    }

    private UsuarioDto toDto(UserRepresentation user) {
        List<String> roles = obtenerRoles(user.getId());

        // Leer el atributo personalizado foto_url de Keycloak (puede ser null)
        String fotoUrl = null;
        if (user.getAttributes() != null) {
            List<String> valores = user.getAttributes().get("foto_url");
            if (valores != null && !valores.isEmpty()) {
                fotoUrl = valores.get(0);
            }
        }

        return new UsuarioDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                Boolean.TRUE.equals(user.isEnabled()),
                roles,
                fotoUrl
        );
    }

    private List<String> obtenerRoles(String userId) {
        if (userId == null) return List.of();
        return realmResource.users().get(userId).roles().realmLevel().listEffective()
                .stream()
                .map(RoleRepresentation::getName)
                .filter(r -> r.equals("USER") || r.equals("ADMIN"))
                .toList();
    }
}
