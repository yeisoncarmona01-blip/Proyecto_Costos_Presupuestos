package com.denkitronik.softcostservice.domain.services;

import com.denkitronik.softcostservice.domain.entities.Usuario;
import com.denkitronik.softcostservice.domain.exception.UsuarioAlreadyExistsException;
import com.denkitronik.softcostservice.domain.exception.UsuarioInvalidCredentialsException;
import com.denkitronik.softcostservice.domain.repositories.IUsuarioDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests — UsuarioServiceImpl con Mockito")
class UsuarioServiceImplTest {

  @Mock
  private IUsuarioDao repository;

  @InjectMocks
  private UsuarioServiceImpl service;

  private Usuario usuario;

  @BeforeEach
  void setUp() {
    usuario = new Usuario();
    usuario.setId(1L);
    usuario.setFullName("Linus Torvalds");
    usuario.setEmail("linus@kernel.org");
    usuario.setOrganization("Kernel");
    usuario.setPasswordHash("existing-hash");
    usuario.setCreatedAt(LocalDateTime.of(2024, 1, 1, 0, 0));
    usuario.setLastLoginAt(LocalDateTime.of(2024, 1, 2, 0, 0));
    usuario.setLoginCount(2);
  }

  @Test
  @DisplayName("register — correo libre → guarda usuario normalizado")
  void register_emailLibre_debeGuardarUsuario() {
    Usuario nuevo = new Usuario();
    nuevo.setFullName("Ada Lovelace");
    nuevo.setEmail("  Ada@Example.com  ");
    nuevo.setOrganization("Math");

    when(repository.findByEmail("ada@example.com"))
      .thenReturn(Optional.empty());
    when(repository.save(any(Usuario.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    Usuario resultado = service.register(nuevo, "Secret123");

    assertThat(resultado.getEmail()).isEqualTo("ada@example.com");
    assertThat(resultado.getPasswordHash())
      .isEqualTo(sha256("Secret123"));
    assertThat(resultado.getCreatedAt()).isNotNull();
    assertThat(resultado.getLoginCount()).isEqualTo(0);

    verify(repository, times(1)).findByEmail("ada@example.com");
    verify(repository, times(1)).save(any(Usuario.class));
  }

  @Test
  @DisplayName("register — correo existente → lanza excepción")
  void register_emailExistente_debeLanzarExcepcion() {
    when(repository.findByEmail("linus@kernel.org"))
      .thenReturn(Optional.of(usuario));

    assertThatThrownBy(() -> service.register(usuario, "Secret123"))
      .isInstanceOf(UsuarioAlreadyExistsException.class)
      .hasMessageContaining("linus@kernel.org");

    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("login — credenciales válidas → actualiza loginCount")
  void login_credencialesValidas_debeActualizarUsuario() {
    Usuario autenticado = new Usuario();
    autenticado.setId(1L);
    autenticado.setFullName("Linus Torvalds");
    autenticado.setEmail("linus@kernel.org");
    autenticado.setPasswordHash(sha256("Secret123"));
    autenticado.setLoginCount(2);

    when(repository.findByEmail("linus@kernel.org"))
      .thenReturn(Optional.of(autenticado));
    when(repository.save(any(Usuario.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    Usuario resultado = service.login("  LINUS@kernel.org ", "Secret123");

    assertThat(resultado.getLoginCount()).isEqualTo(3);
    assertThat(resultado.getLastLoginAt()).isNotNull();

    verify(repository, times(1)).findByEmail("linus@kernel.org");
    verify(repository, times(1)).save(any(Usuario.class));
  }

  @Test
  @DisplayName("login — correo inexistente → lanza excepción")
  void login_correoInexistente_debeLanzarExcepcion() {
    when(repository.findByEmail("linus@kernel.org"))
      .thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.login("linus@kernel.org", "Secret123"))
      .isInstanceOf(UsuarioInvalidCredentialsException.class)
      .hasMessageContaining("inválidos");

    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("login — contraseña incorrecta → lanza excepción")
  void login_contrasenaIncorrecta_debeLanzarExcepcion() {
    Usuario autenticado = new Usuario();
    autenticado.setEmail("linus@kernel.org");
    autenticado.setPasswordHash(sha256("OtraClave"));
    autenticado.setLoginCount(2);

    when(repository.findByEmail("linus@kernel.org"))
      .thenReturn(Optional.of(autenticado));

    assertThatThrownBy(() -> service.login("linus@kernel.org", "Secret123"))
      .isInstanceOf(UsuarioInvalidCredentialsException.class)
      .hasMessageContaining("inválidos");

    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("findAll — retorna todos los usuarios")
  void findAll_debeRetornarLista() {
    when(repository.findAll())
      .thenReturn(List.of(usuario));

    List<Usuario> resultado = service.findAll();

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getEmail())
      .isEqualTo("linus@kernel.org");

    verify(repository, times(1)).findAll();
  }

  @Test
  @DisplayName("findById — ID existente → retorna usuario")
  void findById_idExistente_debeRetornarUsuario() {
    when(repository.findById(1L))
      .thenReturn(Optional.of(usuario));

    Usuario resultado = service.findById(1L);

    assertThat(resultado.getId()).isEqualTo(1L);
    assertThat(resultado.getFullName()).isEqualTo("Linus Torvalds");

    verify(repository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("findById — ID inexistente → lanza RuntimeException")
  void findById_idInexistente_debeLanzarExcepcion() {
    when(repository.findById(999L))
      .thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.findById(999L))
      .isInstanceOf(RuntimeException.class)
      .hasMessageContaining("999");
  }

  private String sha256(String rawPassword) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashed);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
}