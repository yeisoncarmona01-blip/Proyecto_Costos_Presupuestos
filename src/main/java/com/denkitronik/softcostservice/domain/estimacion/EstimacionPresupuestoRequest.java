package com.denkitronik.softcostservice.domain.estimacion;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EstimacionPresupuestoRequest {

  @JsonProperty("nombre_proyecto")
  @JsonAlias({"nombreProyecto"})
  @NotBlank(message = "El nombre del proyecto es obligatorio")
  private String nombreProyecto;

  @JsonProperty("duracion_meses")
  @JsonAlias({"duracionMeses"})
  @NotNull(message = "La duración es obligatoria")
  @Min(value = 1, message = "La duración debe ser mayor a 0")
  private Integer duracionMeses;

  @JsonProperty("tamano_equipo")
  @JsonAlias({"tamanoEquipo"})
  @NotNull(message = "El tamaño del equipo es obligatorio")
  @Min(value = 1, message = "El tamaño del equipo debe ser mayor a 0")
  private Integer tamanoEquipo;

  @JsonProperty("salario_promedio_mensual")
  @JsonAlias({"salarioPromedio"})
  @NotNull(message = "El salario promedio es obligatorio")
  @DecimalMin(value = "0.0", inclusive = false,
    message = "El salario promedio debe ser mayor a 0")
  private Double salarioPromedio;

  @JsonProperty("infraestructura_mensual")
  @JsonAlias({"infraestructuraMensual"})
  @NotNull(message = "La infraestructura mensual es obligatoria")
  @DecimalMin(value = "0.0", message = "La infraestructura mensual no puede ser negativa")
  private Double infraestructuraMensual;

  @JsonProperty("licencias_mensuales")
  @JsonAlias({"licenciasMensuales"})
  @NotNull(message = "Las licencias mensuales son obligatorias")
  @DecimalMin(value = "0.0", message = "Las licencias mensuales no pueden ser negativas")
  private Double licenciasMensuales;

  @JsonProperty("contingencia_porcentaje")
  @JsonAlias({"contingenciaPorcentaje"})
  @NotNull(message = "El porcentaje de contingencia es obligatorio")
  @DecimalMin(value = "0.0", message = "La contingencia no puede ser negativa")
  private Double contingenciaPorcentaje;
}