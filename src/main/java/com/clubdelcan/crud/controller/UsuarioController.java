package com.clubdelcan.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @GetMapping
    public String dashboard() {
        return "usuario/dashboard";
    }

    @GetMapping("/mascotas")
    public String misMascotas(Authentication authentication) {
        String email = authentication.getName();
        String encoded = UriUtils.encode(email, StandardCharsets.UTF_8);
        return "redirect:/mascotas?ownerEmail=" + encoded;
    }
}
