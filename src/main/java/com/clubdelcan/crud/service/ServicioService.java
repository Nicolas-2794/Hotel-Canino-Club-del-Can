package com.clubdelcan.crud.service;

import com.clubdelcan.crud.entity.Servicio;
import com.clubdelcan.crud.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository repo;

    public ServicioService(ServicioRepository repo) {
        this.repo = repo;
    }

    public List<Servicio> listar() {
        return repo.findAll();
    }

    public Servicio obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Servicio crear(Servicio s) {
        validar(s);
        s.setId(null);
        return repo.save(s);
    }

    public Servicio actualizar(Long id, Servicio cambios) {
        Servicio actual = repo.findById(id).orElseThrow();
        validar(cambios);

        actual.setNombre(cambios.getNombre());
        actual.setDescripcion(cambios.getDescripcion());
        actual.setPrecio(cambios.getPrecio());

        return repo.save(actual);
    }

    public Servicio guardar(Servicio s) {
        validar(s);
        return repo.save(s);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    private void validar(Servicio s) {
        if (s.getNombre() == null || s.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del servicio es obligatorio");
        }
        if (s.getPrecio() == null || s.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0");
        }
    }
}
