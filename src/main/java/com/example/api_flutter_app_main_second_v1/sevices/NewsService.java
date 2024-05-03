package com.example.api_flutter_app_main_second_v1.sevices;

import com.example.api_flutter_app_main_second_v1.dtos.NewsBothDTO;

public interface NewsService {
    public NewsBothDTO scrappingData(String department);
}
