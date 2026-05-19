package com.denkitronik.softcostservice.domain.services;

import com.denkitronik.softcostservice.domain.entities.Usuario;
import com.denkitronik.softcostservice.domain.exception.UsuarioAlreadyExistsException;
import com.denkitronik.softcostservice.domain.exception.UsuarioInvalidCredentialsException;
import com.denkitronik.softcostservice.domain.repositories.IUsuarioDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

  private static final Logger log =
    LoggerFactory.getLogger(UsuarioServiceImpl.class);

  @Autowired
  private IUsuarioDao repository;

  @Override
  @Transactional
  public Usuario register(Usuario usuario, String rawPassword) {

    String normalizedEmail = normalizeEmail(usuario.getEmail());

    if (repository.findByEmail(normalizedEmail).isPresent()) {
      throw new UsuarioAlreadyExistsException(normalizedEmail);
    }

    usuario.setEmail(normalizedEmail);
    usuario.setPasswordHash(hashPassword(rawPassword));
    usuario.setCreatedAt(LocalDateTime.now());
    usuario.setLoginCount(0);

    log.info("Registrando usuario: {}", normalizedEmail);

    return repository.save(usuario);
  }

  @Override
  @Transactional
  public Usuario login(String email, String rawPassword) {

    String normalizedEmail = normalizeEmail(email);
    String passwordHash = hashPassword(rawPassword);

    Usuario usuario = repository.findByEmail(normalizedEmail)
      .orElseThrow(() ->
        new UsuarioInvalidCredentialsException());

    if (!passwordHash.equals(usuario.getPasswordHash())) {
      throw new UsuarioInvalidCredentialsException();
    }

    usuario.setLoginCount(usuario.getLoginCount() + 1);
    usuario.setLastLoginAt(LocalDateTime.now());

    log.info("Usuario autenticado: {}", normalizedEmail);

    return repository.save(usuario);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> findAll() {
    return repository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Usuario findById(Long id) {
    return repository.findById(id)
      .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
  }

  private String normalizeEmail(String email) {
    return email == null ? null : email.trim().toLowerCase();
  }

  private String hashPassword(String rawPassword) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashed);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("No se pudo inicializar el hash de contraseña", e);
    }
  }
}