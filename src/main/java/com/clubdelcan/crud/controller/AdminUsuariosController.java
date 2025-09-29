package com.clubdelcan.crud.controller;

import com.clubdelcan.crud.entity.Usuario;
import com.clubdelcan.crud.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

    private final UsuarioService service;

    public AdminUsuariosController(UsuarioService service) { this.service = service; }

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String ok) {
        service.ensureBaseRoles();
        model.addAttribute("usuarios", service.listar());
        model.addAttribute("ok", ok != null);
        return "admin/usuarios-list";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("rolesDisponibles", service.listarRoles());
        return "admin/usuarios-form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("usuario") Usuario u,
                        BindingResult br,
                        @RequestParam(required=false, name="roles") Set<String> roles,
                        Model model) {
        if (br.hasErrors()) {
            model.addAttribute("rolesDisponibles", service.listarRoles());
            return "admin/usuarios-form";
        }
        try {
            service.crear(u, roles);
            return "redirect:/admin/usuarios?ok";
        } catch (IllegalArgumentException ex) {
            br.rejectValue("email", "duplicado", ex.getMessage());
            model.addAttribute("rolesDisponibles", service.listarRoles());
            return "admin/usuarios-form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Usuario u = service.obtener(id).orElse(null);
        if (u == null) return "redirect:/admin/usuarios?notfound";
        model.addAttribute("usuario", u);
        model.addAttribute("rolesDisponibles", service.listarRoles());
        return "admin/usuarios-form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("usuario") Usuario u,
                             BindingResult br,
                             @RequestParam(required=false, name="roles") Set<String> roles,
                             Model model) {
        if (br.hasErrors()) {
            model.addAttribute("rolesDisponibles", service.listarRoles());
            return "admin/usuarios-form";
        }
        service.actualizar(id, u, roles);
        return "redirect:/admin/usuarios?ok";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/admin/usuarios?deleted";
    }
}
