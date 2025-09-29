package com.clubdelcan.crud.repository;

import com.clubdelcan.crud.entity.Reserva;
import com.clubdelcan.crud.entity.Usuario;
import com.clubdelcan.crud.entity.Mascota;
import com.clubdelcan.crud.entity.Servicio;
import com.clubdelcan.crud.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuario(Usuario usuario);
    List<Reserva> findByMascota(Mascota mascota);
    List<Reserva> findByServicio(Servicio servicio);
    List<Reserva> findByHabitacion(Habitacion habitacion);
}
