package com.example.api_flutter_app_main_second_v1.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SetupDataRequest {
    private String userId;
    private String semester;
}
