package com.denkitronik.softcostservice.domain.services;

import com.denkitronik.softcostservice.domain.entities.ProyectoCosto;
import com.denkitronik.softcostservice.domain.exception.ProyectoNotFoundException;
import com.denkitronik.softcostservice.domain.exception.ProyectoServiceException;
import com.denkitronik.softcostservice.domain.repositories.IProyectoCostoDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProyectoCostoServiceImpl implements IProyectoCostoService {

  private static final Logger log =
    LoggerFactory.getLogger(ProyectoCostoServiceImpl.class);

  @Autowired
  private IProyectoCostoDao repository;

  @Override
  @Transactional(readOnly = true)
  public List<ProyectoCosto> findAll() {

    log.info("Listando todos los proyectos");

    return repository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProyectoCosto> findAll(Pageable pageable) {

    log.info("Listando proyectos paginados");

    return repository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public ProyectoCosto findById(Long id) {

    log.info("Buscando proyecto ID: {}", id);

    return repository.findById(id)
      .orElseThrow(() ->
        new ProyectoNotFoundException(id));
  }

  @Override
  @Transactional
  public ProyectoCosto save(ProyectoCosto proyectoCosto) {

    log.info("Guardando proyecto: {}",
      proyectoCosto.getNombreProyecto());

    try {

      Double total =
        proyectoCosto.getMateriales()
          + proyectoCosto.getSalarios()
          + proyectoCosto.getInfraestructura()
          + proyectoCosto.getLicencias()
          + proyectoCosto.getOtrosGastos();

      proyectoCosto.setCostoTotal(total);

      ProyectoCosto proyectoGuardado =
        repository.save(proyectoCosto);

      log.info("Proyecto guardado correctamente con ID: {}",
        proyectoGuardado.getId());

      return proyectoGuardado;

    } catch (Exception e) {

      log.error("Error guardando proyecto", e);

      throw new ProyectoServiceException(
        "Error al guardar el proyecto");
    }
  }

  @Override
  @Transactional
  public void delete(Long id) {

    log.info("Eliminando proyecto ID: {}", id);

    ProyectoCosto proyecto = repository.findById(id)
      .orElseThrow(() ->
        new ProyectoNotFoundException(id));

    try {

      repository.delete(proyecto);

      log.info("Proyecto eliminado correctamente");

    } catch (Exception e) {

      log.error("Error eliminando proyecto", e);

      throw new ProyectoServiceException(
        "Error al eliminar el proyecto");
    }
  }
}
