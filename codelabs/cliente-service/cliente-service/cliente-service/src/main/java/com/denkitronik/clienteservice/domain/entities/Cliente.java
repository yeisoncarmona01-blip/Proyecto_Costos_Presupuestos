package com.denkitronik.clienteservice.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@Schema(description = "Entidad cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID generado automaticamente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    @Column(nullable = false)
    @Schema(description = "Nombre del cliente", example = "Ada")
    private String nombre;

    @NotEmpty(message = "El apellido no puede estar vacio")
    @Schema(description = "Apellido del cliente", example = "Lovelace")
    private String apellido;

    @NotEmpty(message = "El email no puede estar vacio")
    @Email(message = "Debe ser una direccion de email valida")
    @Column(nullable = false, unique = true)
    @Schema(description = "Email unico del cliente", example = "ada@babbage.uk")
    private String email;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    @Schema(description = "Fecha de creacion (asignada automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private Date createAt;

    @Schema(description = "Nombre del archivo de foto", example = "ada.jpg")
    private String foto;

    @NotNull(message = "La region es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Region a la que pertenece el cliente")
    private Region region;

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }
}