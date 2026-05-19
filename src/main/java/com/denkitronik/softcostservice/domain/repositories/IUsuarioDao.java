package com.denkitronik.softcostservice.domain.repositories;

import com.denkitronik.softcostservice.domain.entities.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

  Optional<Usuario> findByEmail(String email);
}