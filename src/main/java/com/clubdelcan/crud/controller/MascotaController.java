package com.clubdelcan.crud.controller;

import com.clubdelcan.crud.entity.Mascota;
import com.clubdelcan.crud.entity.Usuario;
import com.clubdelcan.crud.repository.UsuarioRepository;
import com.clubdelcan.crud.service.MascotaService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaService service;
    private final UsuarioRepository usuarioRepo;

    public MascotaController(MascotaService service, UsuarioRepository usuarioRepo) {
        this.service = service;
        this.usuarioRepo = usuarioRepo;
    }

    /** Lista: ADMIN ve todas, CLIENTE ve solo las suyas */
    @GetMapping
    public String listar(Model model, Authentication auth) {
        boolean esAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (esAdmin) {
            model.addAttribute("mascotas", service.listarTodas());
        } else {
            var uOpt = usuarioRepo.findByEmail(auth.getName());
            if (uOpt.isEmpty()) {
                model.addAttribute("mascotas", java.util.List.of());
                model.addAttribute("warning",
                        "Tu usuario no existe en la base de datos. Para la demo, crea un usuario con tu email en /admin/usuarios.");
            } else {
                model.addAttribute("mascotas", service.listarPorUsuario(uOpt.get()));
            }
        }
        return "mascotas/mascotas-list";
    }

    /** Form crear */
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("mascota", new Mascota());
        return "mascotas/mascotas-form";
    }

    /** Crear: asocia la mascota al usuario logeado (CLIENTE o ADMIN creando para sí mismo) */
    @PostMapping
    public String crear(@Valid @ModelAttribute("mascota") Mascota mascota,
                        BindingResult br,
                        Authentication auth,
                        Model model) {
        if (br.hasErrors()) {
            return "mascotas/mascotas-form";
        }

        var uOpt = usuarioRepo.findByEmail(auth.getName());
        if (uOpt.isEmpty()) {
            model.addAttribute("error",
                    "Tu usuario no existe en la base de datos. Para la demo, crea un usuario en /admin/usuarios con el mismo email con el que inicias sesión.");
            model.addAttribute("mascota", mascota);
            return "mascotas/mascotas-form";
        }

        mascota.setUsuario(uOpt.get());
        service.guardar(mascota);
        return "redirect:/mascotas?ok";
    }

    /** Editar: sólo el dueño o ADMIN */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, Authentication auth) {
        Mascota m = service.obtener(id);
        if (m == null) return "redirect:/mascotas?notfound";

        boolean esAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!esAdmin && !m.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/mascotas?forbidden";
        }

        model.addAttribute("mascota", m);
        return "mascotas/mascotas-form";
    }

    /** Actualizar: respeta el propietario */
    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("mascota") Mascota mascota,
                             BindingResult br,
                             Authentication auth) {
        if (br.hasErrors()) {
            return "mascotas/mascotas-form";
        }
        Mascota actual = service.obtener(id);
        if (actual == null) return "redirect:/mascotas?notfound";

        boolean esAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!esAdmin && !actual.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/mascotas?forbidden";
        }

        // Mantener el propietario original
        mascota.setId(id);
        mascota.setUsuario(actual.getUsuario());
        service.guardar(mascota);
        return "redirect:/mascotas?updated";
    }

    /** Eliminar: sólo dueño o ADMIN */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, Authentication auth) {
        Mascota m = service.obtener(id);
        if (m == null) return "redirect:/mascotas?notfound";

        boolean esAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!esAdmin && !m.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/mascotas?forbidden";
        }
        service.eliminar(id);
        return "redirect:/mascotas?deleted";
    }
}
