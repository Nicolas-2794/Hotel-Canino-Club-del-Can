package com.clubdelcan.crud.controller;

import com.clubdelcan.crud.entity.Habitacion;
import com.clubdelcan.crud.service.HabitacionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/habitaciones")
public class HabitacionAdminController {

    private final HabitacionService service;

    public HabitacionAdminController(HabitacionService service) {
        this.service = service;
    }

    /** Listado */
    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String ok) {
        model.addAttribute("habitaciones", service.listar());
        model.addAttribute("ok", ok != null);
        return "admin/habitaciones-list";
    }

    /** Formulario nueva */
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("habitacion", new Habitacion());
        return "admin/habitaciones-form";
    }

    /** Crear */
    @PostMapping
    public String crear(@Valid @ModelAttribute("habitacion") Habitacion h,
                        BindingResult br,
                        Model model) {
        if (!service.numeroDisponibleParaCrear(h.getNumero())) {
            br.rejectValue("numero", "duplicado", "Ya existe una habitación con ese número");
        }
        if (br.hasErrors()) {
            return "admin/habitaciones-form";
        }
        try {
            service.guardar(h);
            return "redirect:/admin/habitaciones?ok";
        } catch (IllegalArgumentException ex) {
            br.rejectValue("numero", "duplicado", ex.getMessage());
            return "admin/habitaciones-form";
        } catch (IllegalStateException ex) {
            model.addAttribute("globalError", ex.getMessage());
            return "admin/habitaciones-form";
        }
    }

    /** Editar */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Habitacion h = service.obtener(id);
        if (h == null) return "redirect:/admin/habitaciones?notfound";
        model.addAttribute("habitacion", h);
        return "admin/habitaciones-form";
    }

    /** Actualizar */
    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("habitacion") Habitacion h,
                             BindingResult br,
                             Model model) {
        if (!service.numeroDisponibleParaEditar(id, h.getNumero())) {
            br.rejectValue("numero", "duplicado", "Ya existe otra habitación con ese número");
        }
        if (br.hasErrors()) {
            return "admin/habitaciones-form";
        }
        try {
            h.setId(id);
            service.guardar(h);
            return "redirect:/admin/habitaciones?ok";
        } catch (IllegalArgumentException ex) {
            br.rejectValue("numero", "duplicado", ex.getMessage());
            return "admin/habitaciones-form";
        } catch (IllegalStateException ex) {
            model.addAttribute("globalError", ex.getMessage());
            return "admin/habitaciones-form";
        }
    }

    /** Eliminar */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/admin/habitaciones?deleted";
    }
}
