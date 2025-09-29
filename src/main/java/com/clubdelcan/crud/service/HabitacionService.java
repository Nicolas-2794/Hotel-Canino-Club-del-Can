package com.clubdelcan.crud.service;

import com.clubdelcan.crud.entity.Habitacion;
import com.clubdelcan.crud.entity.HotelInfo;
import com.clubdelcan.crud.repository.HabitacionRepository;
import com.clubdelcan.crud.repository.HotelInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HabitacionService {

    private final HabitacionRepository repo;
    private final HotelInfoRepository hotelRepo;

    public HabitacionService(HabitacionRepository repo, HotelInfoRepository hotelRepo) {
        this.repo = repo;
        this.hotelRepo = hotelRepo;
    }

    @Transactional(readOnly = true)
    public List<Habitacion> listar(){ return repo.findAll(); }

    @Transactional(readOnly = true)
    public Habitacion obtener(Long id){ return repo.findById(id).orElse(null); }

    public Habitacion guardar(Habitacion h){
        if (h.getHotel() == null) {
            HotelInfo hotel = hotelRepo.findById(1L)
                    .orElseThrow(() -> new IllegalStateException("No existe el hotel base (id=1)."));
            h.setHotel(hotel);
        }

        // Crear
        if (h.getId() == null) {
            if (repo.existsByNumero(h.getNumero())) {
                throw new IllegalArgumentException("Ya existe una habitación con ese número.");
            }
            return repo.save(h);
        }
        // Editar
        if (repo.existsByNumeroAndIdNot(h.getNumero(), h.getId())) {
            throw new IllegalArgumentException("Ya existe otra habitación con ese número.");
        }
        return repo.save(h);
    }

    public void eliminar(Long id){ repo.deleteById(id); }

    @Transactional(readOnly = true)
    public boolean numeroDisponibleParaCrear(String numero){
        return !repo.existsByNumero(numero);
    }

    @Transactional(readOnly = true)
    public boolean numeroDisponibleParaEditar(Long id, String numero){
        return !repo.existsByNumeroAndIdNot(numero, id);
    }
}
