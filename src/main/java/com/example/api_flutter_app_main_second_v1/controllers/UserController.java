package com.example.api_flutter_app_main_second_v1.controllers;

import com.example.api_flutter_app_main_second_v1.dtos.UserDTO;
import com.example.api_flutter_app_main_second_v1.requests.SetupDataRequest;
import com.example.api_flutter_app_main_second_v1.sevices.ScraperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    final ScraperService scraperService;

    public UserController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @PostMapping("/setupData")
    public ResponseEntity<UserDTO> setUpData(@RequestBody SetupDataRequest request) {
        UserDTO userDTO =  scraperService.scrappingData(request);
        if(userDTO == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/semester")
    public List<String> getAllSemester() {
        return scraperService.getSemesterList();
    }
}
