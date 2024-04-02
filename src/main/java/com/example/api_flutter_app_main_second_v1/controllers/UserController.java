package com.example.api_flutter_app_main_second_v1.controllers;

import com.example.api_flutter_app_main_second_v1.dtos.UserDTO;
import com.example.api_flutter_app_main_second_v1.sevices.ScraperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    final ScraperService scraperService;

    public UserController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/hello")
    public UserDTO helloUser() {
        return scraperService.scrappingData("6656485");
    }
}
