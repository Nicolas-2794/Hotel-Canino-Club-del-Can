package com.clubdelcan.crud.controller;

import com.clubdelcan.crud.entity.Servicio;
import com.clubdelcan.crud.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService service;
    public ServicioController(ServicioService service){ this.service = service; }

    @GetMapping
    public String listar(Model model){
        model.addAttribute("servicios", service.listar());
        return "servicios/servicios-list";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model){
        model.addAttribute("servicio", new Servicio());
        return "servicios/servicios-form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("servicio") Servicio servicio,
                        BindingResult br,
                        RedirectAttributes ra){
        if (br.hasErrors()) return "servicios/servicios-form";
        try {
            service.guardar(servicio);
            ra.addFlashAttribute("ok", "Servicio creado");
            return "redirect:/servicios";
        } catch (IllegalArgumentException ex) {
            br.rejectValue("precio", null, ex.getMessage());
            return "servicios/servicios-form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model){
        Servicio s = service.obtener(id);
        if (s == null) return "redirect:/servicios?notfound";
        model.addAttribute("servicio", s);
        return "servicios/servicios-form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("servicio") Servicio servicio,
                             BindingResult br,
                             RedirectAttributes ra){
        if (br.hasErrors()) return "servicios/servicios-form";
        try {
            servicio.setId(id);
            service.guardar(servicio);
            ra.addFlashAttribute("ok", "Servicio actualizado");
            return "redirect:/servicios";
        } catch (IllegalArgumentException ex) {
            br.rejectValue("precio", null, ex.getMessage());
            return "servicios/servicios-form";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra){
        service.eliminar(id);
        ra.addFlashAttribute("ok", "Servicio eliminado");
        return "redirect:/servicios";
    }
}
