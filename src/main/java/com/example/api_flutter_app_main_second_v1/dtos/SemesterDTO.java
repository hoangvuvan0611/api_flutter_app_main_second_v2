package com.example.api_flutter_app_main_second_v1.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SemesterDTO {
    private String semesterName;
    private String gpa;
    private String totalCredit;
    @JsonIgnore
    private UserDTO user;
    private List<ScoreDTO> scoreList;
}
