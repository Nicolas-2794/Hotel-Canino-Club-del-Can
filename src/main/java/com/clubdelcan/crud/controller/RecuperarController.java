package com.clubdelcan.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class RecuperarController {

    @GetMapping("/recuperar")
    public String formRecuperar() {
        return "auth/recuperar-form";
    }

    @PostMapping("/recuperar")
    public String procesarRecuperar(@RequestParam String email, Model model) {
        // DEMO: generamos un "token" y mostramos un mensaje convincente
        String tokenDemo = UUID.randomUUID().toString().substring(0, 8);
        model.addAttribute("email", email);
        model.addAttribute("token", tokenDemo);
        return "auth/recuperar-enviado";
    }
}
