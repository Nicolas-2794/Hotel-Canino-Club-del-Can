package com.clubdelcan.crud.controller;

import com.clubdelcan.crud.entity.Mascota;
import com.clubdelcan.crud.entity.Reserva;
import com.clubdelcan.crud.entity.Servicio;
import com.clubdelcan.crud.service.MascotaService;
import com.clubdelcan.crud.service.ReservaService;
import com.clubdelcan.crud.service.ServicioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final MascotaService mascotaService;
    private final ServicioService servicioService;

    public ReservaController(ReservaService r, MascotaService m, ServicioService s) {
        this.reservaService = r;
        this.mascotaService = m;
        this.servicioService = s;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("reservas", reservaService.listar());
        return "reservas/reservas-list";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("mascotas", mascotaService.listarTodas());
        model.addAttribute("servicios", servicioService.listar());
        return "reservas/reservas-form";
    }

    @PostMapping
    public String crear(@RequestParam Long mascotaId,
                        @RequestParam(required = false) Long servicioId, // servicio es opcional
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                        Model model) {

        // Validación de fechas
        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            model.addAttribute("error", "Rango de fechas inválido");
            model.addAttribute("reserva", new Reserva());
            model.addAttribute("mascotas", mascotaService.listarTodas());
            model.addAttribute("servicios", servicioService.listar());
            return "reservas/reservas-form";
        }

        // Mascota obligatoria
        Mascota mascota = mascotaService.obtener(mascotaId);
        if (mascota == null) {
            model.addAttribute("error", "Mascota inválida");
            model.addAttribute("reserva", new Reserva());
            model.addAttribute("mascotas", mascotaService.listarTodas());
            model.addAttribute("servicios", servicioService.listar());
            return "reservas/reservas-form";
        }

        // Servicio opcional
        Servicio servicio = null;
        if (servicioId != null) {
            servicio = servicioService.obtener(servicioId);
            if (servicio == null) {
                model.addAttribute("error", "Servicio inválido");
                model.addAttribute("reserva", new Reserva());
                model.addAttribute("mascotas", mascotaService.listarTodas());
                model.addAttribute("servicios", servicioService.listar());
                return "reservas/reservas-form";
            }
        }

        // Construcción de la reserva (usuario viene desde la mascota para cumplir @NotNull)
        Reserva r = Reserva.builder()
                .mascota(mascota)
                .usuario(mascota.getUsuario())
                .servicio(servicio) // puede ser null
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .estado(Reserva.Estado.PENDIENTE)
                .build();

        reservaService.guardar(r);
        return "redirect:/reservas?ok";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam Reserva.Estado value) {
        Reserva r = reservaService.obtener(id);
        if (r != null) {
            r.setEstado(value);
            reservaService.guardar(r);
        }
        return "redirect:/reservas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return "redirect:/reservas?deleted";
    }
}
