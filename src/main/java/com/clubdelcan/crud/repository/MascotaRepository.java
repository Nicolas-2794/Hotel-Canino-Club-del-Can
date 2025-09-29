package com.clubdelcan.crud.repository;

import com.clubdelcan.crud.entity.Mascota;
import com.clubdelcan.crud.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByUsuario(Usuario usuario);
}
