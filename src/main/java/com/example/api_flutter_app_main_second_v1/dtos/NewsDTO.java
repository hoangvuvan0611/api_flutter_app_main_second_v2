package com.example.api_flutter_app_main_second_v1.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewsDTO {
    private String title;
    private String url;
    private String date;
}
