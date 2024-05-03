package com.example.api_flutter_app_main_second_v1.controllers;

import com.example.api_flutter_app_main_second_v1.dtos.NewsBothDTO;
import com.example.api_flutter_app_main_second_v1.sevices.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsController {
    final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/department={department}")
    public ResponseEntity<NewsBothDTO> setUpData(@PathVariable String department) {
        NewsBothDTO newsBothDTO =  newsService.scrappingData(department);
        if(newsBothDTO == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(newsBothDTO);
    }
}
