package com.clubdelcan.crud.repository;

import com.clubdelcan.crud.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    boolean existsByNumero(String numero);
    boolean existsByNumeroAndIdNot(String numero, Long id);
    Optional<Habitacion> findByNumero(String numero);
}

