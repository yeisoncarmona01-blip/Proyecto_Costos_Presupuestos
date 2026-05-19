package com.denkitronik.softcostservice.domain.repositories;

import com.denkitronik.softcostservice.domain.entities.ProyectoCosto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IProyectoCostoDao
  extends JpaRepository<ProyectoCosto, Long> {

}
