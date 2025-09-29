package com.clubdelcan.crud.service;

import com.clubdelcan.crud.entity.Reserva;
import com.clubdelcan.crud.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository repo;

    public ReservaService(ReservaRepository repo) {
        this.repo = repo;
    }

    public List<Reserva> listar() {
        return repo.findAll();
    }

    public Reserva obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Reserva guardar(Reserva r) {
        return repo.save(r);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
