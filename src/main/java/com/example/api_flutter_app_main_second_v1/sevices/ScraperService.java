package com.example.api_flutter_app_main_second_v1.sevices;

import com.example.api_flutter_app_main_second_v1.dtos.UserDTO;

import java.io.IOException;

public interface ScraperService {
    public UserDTO scrappingData(String userCode);
}
