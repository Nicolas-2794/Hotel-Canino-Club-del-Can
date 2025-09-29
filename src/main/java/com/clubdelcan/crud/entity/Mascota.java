package com.clubdelcan.crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "mascota")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 50)
    private String raza;

    @Column(length = 10)
    private String sexo;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser positivo")
    @Digits(integer = 5, fraction = 2)
    @Column(name = "peso_kg")
    private BigDecimal pesoKg;

    @NotNull(message = "Indique si tiene vacunas al día")
    @Column(name = "vacunasaldia", nullable = false)
    private Boolean vacunasAlDia;

    // Relación con Usuario
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
