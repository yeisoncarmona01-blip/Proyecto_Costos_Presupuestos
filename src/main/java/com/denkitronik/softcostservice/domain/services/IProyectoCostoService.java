package com.denkitronik.softcostservice.domain.services;

import com.denkitronik.softcostservice.domain.entities.ProyectoCosto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProyectoCostoService {

  List<ProyectoCosto> findAll();

  Page<ProyectoCosto> findAll(Pageable pageable);

  ProyectoCosto findById(Long id);

  ProyectoCosto save(ProyectoCosto proyectoCosto);

  void delete(Long id);
}
