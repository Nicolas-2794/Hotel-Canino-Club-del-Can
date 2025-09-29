// src/main/java/com/clubdelcan/crud/service/MascotaService.java
package com.clubdelcan.crud.service;

import com.clubdelcan.crud.entity.Mascota;
import com.clubdelcan.crud.entity.Usuario;
import com.clubdelcan.crud.repository.MascotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MascotaService {

    private final MascotaRepository repo;

    public MascotaService(MascotaRepository repo) {
        this.repo = repo;
    }

    // -------- ADMIN: listar todas las mascotas
    public List<Mascota> listarTodas() {
        return repo.findAll();
    }

    // -------- CLIENTE: listar solo sus mascotas
    public List<Mascota> listarPorUsuario(Usuario usuario) {
        return repo.findByUsuario(usuario);
    }

    // Obtener mascota por ID
    public Mascota obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Crear o actualizar mascota
    public Mascota guardar(Mascota m) {
        return repo.save(m);
    }

    // Eliminar mascota
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
