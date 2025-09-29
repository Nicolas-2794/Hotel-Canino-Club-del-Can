package com.clubdelcan.crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reserva {

    public enum Estado { PENDIENTE, CONFIRMADA, ANULADA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    @NotNull
    private Mascota mascota;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;

    @Column(name = "fecha_inicio", nullable = false)
    @NotNull
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @NotNull
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Estado estado = Estado.PENDIENTE;

    @PrePersist @PreUpdate
    private void validarFechas() {
        if (fechaInicio != null && fechaFin != null && !fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("fecha_fin debe ser posterior a fecha_inicio");
        }
    }
}
