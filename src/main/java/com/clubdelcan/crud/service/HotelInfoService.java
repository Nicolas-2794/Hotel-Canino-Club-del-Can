package com.clubdelcan.crud.service;

import com.clubdelcan.crud.entity.HotelInfo;
import com.clubdelcan.crud.repository.HotelInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelInfoService {

    private final HotelInfoRepository repo;

    public HotelInfoService(HotelInfoRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public HotelInfo getOrCreate() {
        return repo.findById(1L).orElseGet(() -> {
            HotelInfo def = HotelInfo.builder()
                    .id(1L)
                    .nombre("Club del Can")
                    .direccion("Por definir")
                    .telefono("")
                    .email("contacto@clubdelcan.cl")
                    .horario("Lunes a Domingo 09:00 - 19:00")
                    .build();
            return repo.save(def);
        });
    }

    public HotelInfo guardar(HotelInfo info) {
        if (info.getId() == null) info.setId(1L);
        return repo.save(info);
    }
}
