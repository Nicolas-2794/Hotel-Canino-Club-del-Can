package com.clubdelcan.crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "hotel_info")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HotelInfo {

    @Id
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(length = 30)
    private String telefono;

    @Email(message = "Formato de email inválido")
    @Column(length = 120)
    private String email;

    @Column(length = 120)
    private String horario;
}
