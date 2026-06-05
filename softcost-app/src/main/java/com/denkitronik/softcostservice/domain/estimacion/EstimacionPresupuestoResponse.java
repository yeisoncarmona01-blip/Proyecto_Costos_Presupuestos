package com.denkitronik.softcostservice.domain.estimacion;

import lombok.Data;

@Data
public class EstimacionPresupuestoResponse {

  private String nombreProyecto;

  private Double costoPersonal;

  private Double costoInfraestructura;

  private Double costoLicencias;

  private Double subtotal;

  private Double contingenciaMonto;

  private Double totalPresupuesto;

  private Double porcentajePersonal;

  private Double porcentajeInfraestructura;

  private Double porcentajeLicencias;

  private Double porcentajeContingencia;
}