// src/main/java/com/clubdelcan/crud/entity/Servicio.java
package com.clubdelcan.crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servicio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(columnDefinition = "text")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;
}
