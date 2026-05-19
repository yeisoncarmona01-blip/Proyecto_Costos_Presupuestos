package com.denkitronik.softcostservice.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "proyectos_costos")
@Data
public class ProyectoCosto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "El nombre del proyecto no puede estar vacío")
  @Size(min = 4, max = 100)
  @Column(name = "nombre_proyecto")
  private String nombreProyecto;

  @NotNull(message = "Materiales es obligatorio")
  private Double materiales;

  @NotNull(message = "Salarios es obligatorio")
  private Double salarios;

  @NotNull(message = "Infraestructura es obligatorio")
  private Double infraestructura;

  @NotNull(message = "Licencias es obligatorio")
  private Double licencias;

  @NotNull(message = "Otros gastos es obligatorio")
  @Column(name = "otros_gastos")
  private Double otrosGastos;

  @Column(name = "costo_total")
  private Double costoTotal;
}
