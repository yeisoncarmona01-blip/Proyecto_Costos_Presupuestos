package com.denkitronik.softcostservice.delivery.rest;

import com.denkitronik.softcostservice.domain.entities.Usuario;
import com.denkitronik.softcostservice.domain.services.IUsuarioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class UsuarioRestController {

  @Autowired
  private IUsuarioService service;

  @PostMapping("/register")
  public ResponseEntity<?> register(
    @Valid @RequestBody RegisterRequest request,
    BindingResult result) {

    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(result.getAllErrors());
    }

    Usuario usuario = new Usuario();
    usuario.setFullName(request.fullName());
    usuario.setEmail(request.email());
    usuario.setOrganization(request.organization());

    Usuario guardado = service.register(usuario, request.password());

    return new ResponseEntity<>(toResponse(guardado), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(
    @Valid @RequestBody LoginRequest request,
    BindingResult result) {

    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(result.getAllErrors());
    }

    Usuario autenticado = service.login(request.email(), request.password());

    return ResponseEntity.ok(toResponse(autenticado));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    Usuario usuario = service.findById(id);
    return ResponseEntity.ok(toResponse(usuario));
  }

  @GetMapping
  public ResponseEntity<?> listAll() {
    java.util.List<Usuario> usuarios = service.findAll();
    java.util.List<AuthResponse> resp = usuarios.stream()
      .map(this::toResponse)
      .toList();
    return ResponseEntity.ok(resp);
  }

  private AuthResponse toResponse(Usuario usuario) {
    return new AuthResponse(
      usuario.getId(),
      usuario.getFullName(),
      usuario.getEmail(),
      usuario.getOrganization(),
      usuario.getCreatedAt(),
      usuario.getLastLoginAt(),
      usuario.getLoginCount()
    );
  }

  public record RegisterRequest(
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 120)
    String fullName,
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    String email,
    @Size(max = 160)
    String organization,
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 120)
    String password) {
  }

  public record LoginRequest(
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    String email,
    @NotBlank(message = "La contraseña es obligatoria")
    String password) {
  }

  public record AuthResponse(
    Long id,
    String fullName,
    String email,
    String organization,
    LocalDateTime createdAt,
    LocalDateTime lastLoginAt,
    Integer loginCount) {
  }
}