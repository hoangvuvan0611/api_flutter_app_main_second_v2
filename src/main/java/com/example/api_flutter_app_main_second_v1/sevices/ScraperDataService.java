package com.example.api_flutter_app_main_second_v1.sevices;

import com.example.api_flutter_app_main_second_v1.dtos.UserDTO;
import com.example.api_flutter_app_main_second_v1.requests.SetupDataRequest;

import java.util.List;

public interface ScraperDataService {
    UserDTO scrappingData(SetupDataRequest request);
    List<String> getSemesterList();
}
