package com.denkitronik.softcostservice.domain.services;

import com.denkitronik.softcostservice.domain.entities.Usuario;

public interface IUsuarioService {

  Usuario register(Usuario usuario, String rawPassword);

  Usuario login(String email, String rawPassword);

  java.util.List<Usuario> findAll();

  Usuario findById(Long id);
}