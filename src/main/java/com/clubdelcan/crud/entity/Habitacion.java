package com.clubdelcan.crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="habitacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="El número es obligatorio")
    @Column(nullable=false, length=20, unique=true)
    private String numero;

    @NotBlank(message="El tipo es obligatorio")
    @Column(nullable=false, length=40)
    private String tipo;

    @NotNull(message="La capacidad es obligatoria")
    @Min(value=1, message="Capacidad mínima 1")
    private Integer capacidad;

    @NotNull(message="El precio por noche es obligatorio")
    @DecimalMin(value="0.0", message="El precio no puede ser negativo")
    private BigDecimal precioNoche;

    @Column(length=200)
    private String descripcion;

    @NotNull
    @Builder.Default
    @Column(nullable=false)
    private Boolean disponible = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelInfo hotel;
}
