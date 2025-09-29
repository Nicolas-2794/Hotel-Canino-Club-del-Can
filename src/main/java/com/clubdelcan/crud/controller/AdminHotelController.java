package com.clubdelcan.crud.controller;

import com.clubdelcan.crud.entity.HotelInfo;
import com.clubdelcan.crud.service.HotelInfoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/hotel")
public class AdminHotelController {

    private final HotelInfoService service;

    public AdminHotelController(HotelInfoService service) { this.service = service; }

    @GetMapping
    public String form(Model model, @ModelAttribute("ok") String ok) {
        HotelInfo info = service.getOrCreate();
        model.addAttribute("hotel", info);
        model.addAttribute("ok", "1".equals(ok));
        return "admin/hotel-form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("hotel") HotelInfo hotel,
                       BindingResult br,
                       RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "admin/hotel-form";
        }
        hotel.setId(1L);
        service.guardar(hotel);
        ra.addFlashAttribute("ok", "1");
        return "redirect:/admin/hotel";
    }
}
