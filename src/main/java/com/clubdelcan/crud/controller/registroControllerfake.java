package com.clubdelcan.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class registroControllerfake {
    @GetMapping("/registro-falso")
    public String fake(){
        return "registro-falso";
    }
}
