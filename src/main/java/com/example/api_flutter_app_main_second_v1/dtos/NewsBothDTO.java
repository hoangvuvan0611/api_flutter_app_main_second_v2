package com.example.api_flutter_app_main_second_v1.dtos;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewsBothDTO {
    private List<NewsDTO> universityNewsList;
    private List<NewsDTO> departmentNewsList;
    private String universityNewsLinkAll;
    private String departmentNewsLinkAll;
}
