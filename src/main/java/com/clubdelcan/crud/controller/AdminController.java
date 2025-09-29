package com.clubdelcan.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }
//    @GetMapping("/usuarios")     public String usuarios()          { return "admin/usuarios"; }
    @GetMapping("/mascotas")     public String adminMascotas()     { return "redirect:/mascotas"; }
    @GetMapping("/reservas")     public String adminReservas()     { return "redirect:/reservas"; }
    @GetMapping("/servicios")    public String adminServicios()    { return "redirect:/servicios"; }
//    @GetMapping("/habitaciones") public String habitaciones()      { return "admin/habitaciones"; }
//    @GetMapping("/hotel")        public String hotel()             { return "admin/hotel"; }


//    @GetMapping("/usuarios")
//    public String usuarios() {
//        return "admin/usuarios"; // placeholder
//    }
//
//    @GetMapping("/hotel")
//    public String hotel() {
//        return "admin/hotel"; // placeholder
//    }
}
