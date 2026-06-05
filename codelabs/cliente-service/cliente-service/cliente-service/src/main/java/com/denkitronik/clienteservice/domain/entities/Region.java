package com.denkitronik.clienteservice.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regiones")
@Getter
@Setter
@Schema(description = "Region geografica")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la region", example = "1")
    private Long id;

    @Schema(description = "Nombre de la region", example = "America del Sur")
    private String nombre;
}