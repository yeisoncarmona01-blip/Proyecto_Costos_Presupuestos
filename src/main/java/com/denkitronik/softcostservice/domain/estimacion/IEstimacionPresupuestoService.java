package com.denkitronik.softcostservice.domain.estimacion;

public interface IEstimacionPresupuestoService {

  EstimacionPresupuestoResponse calcular(EstimacionPresupuestoRequest request);

  /**
   * Comprueba si la librería nativa puede cargarse y está disponible.
   */
  boolean isNativeAvailable();
}