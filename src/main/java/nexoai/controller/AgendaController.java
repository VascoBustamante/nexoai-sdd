package nexoai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AgendaController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/agenda";
    }

    @GetMapping("/agenda")
    public String agenda() {
        return "agenda";
    }
}