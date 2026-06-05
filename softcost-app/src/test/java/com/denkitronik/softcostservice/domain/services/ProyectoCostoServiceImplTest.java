package com.denkitronik.softcostservice.domain.services;

import com.denkitronik.softcostservice.domain.entities.ProyectoCosto;
import com.denkitronik.softcostservice.domain.exception.ProyectoNotFoundException;
import com.denkitronik.softcostservice.domain.exception.ProyectoServiceException;
import com.denkitronik.softcostservice.domain.repositories.IProyectoCostoDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests — ProyectoCostoServiceImpl")
class ProyectoCostoServiceImplTest {

  @Mock
  private IProyectoCostoDao repository;

  @InjectMocks
  private ProyectoCostoServiceImpl service;

  private ProyectoCosto proyecto;

  @BeforeEach
  void setUp() {

    proyecto = new ProyectoCosto();

    proyecto.setId(1L);

    proyecto.setNombreProyecto("SOFTCOST ERP");

    proyecto.setMateriales(5000.0);
    proyecto.setSalarios(8000.0);
    proyecto.setInfraestructura(2000.0);
    proyecto.setLicencias(1000.0);
    proyecto.setOtrosGastos(500.0);
  }

  @Test
  @DisplayName("findById — ID existente → retorna proyecto")
  void findById_idExistente_debeRetornarProyecto() {

    when(repository.findById(1L))
      .thenReturn(Optional.of(proyecto));

    ProyectoCosto resultado = service.findById(1L);

    assertThat(resultado.getId()).isEqualTo(1L);

    assertThat(resultado.getNombreProyecto())
      .isEqualTo("SOFTCOST ERP");

    verify(repository, times(1))
      .findById(1L);
  }

  @Test
  @DisplayName("findById — ID inexistente → lanza excepción")
  void findById_idInexistente_debeLanzarExcepcion() {

    when(repository.findById(999L))
      .thenReturn(Optional.empty());

    assertThatThrownBy(() ->
      service.findById(999L))

      .isInstanceOf(ProyectoNotFoundException.class)

      .hasMessageContaining("999");
  }

  @Test
  @DisplayName("save — proyecto válido → guarda correctamente")
  void save_proyectoValido_debeGuardar() {

    when(repository.save(any(ProyectoCosto.class)))
      .thenReturn(proyecto);

    ProyectoCosto resultado = service.save(proyecto);

    assertThat(resultado.getNombreProyecto())
      .isEqualTo("SOFTCOST ERP");

    assertThat(resultado.getCostoTotal())
      .isEqualTo(16500.0);

    verify(repository, times(1))
      .save(any(ProyectoCosto.class));
  }

  @Test
  @DisplayName("save — error BD → lanza ProyectoServiceException")
  void save_errorBD_debeLanzarProyectoServiceException() {

    when(repository.save(any(ProyectoCosto.class)))

      .thenThrow(
        new DataIntegrityViolationException(
          "duplicate key"));

    assertThatThrownBy(() ->
      service.save(proyecto))

      .isInstanceOf(ProyectoServiceException.class);
  }

  @Test
  @DisplayName("delete — ID existente → elimina correctamente")
  void delete_idExistente_debeEliminar() {

    when(repository.findById(1L))
      .thenReturn(Optional.of(proyecto));

    doNothing().when(repository)
      .delete(any(ProyectoCosto.class));

    service.delete(1L);

    verify(repository, times(1))
      .delete(any(ProyectoCosto.class));
  }

  @Test
  @DisplayName("delete — ID inexistente → lanza excepción")
  void delete_idInexistente_debeLanzarExcepcion() {

    when(repository.findById(999L))
      .thenReturn(Optional.empty());

    assertThatThrownBy(() ->
      service.delete(999L))

      .isInstanceOf(ProyectoNotFoundException.class);

    verify(repository, never())
      .delete(any());
  }
}
