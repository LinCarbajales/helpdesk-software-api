package dev.lin.helpdesk_software_api.Home;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HomeController {

    @GetMapping("")
    public String index() {
        return "Hello, Spring Boot";
    }
    
}