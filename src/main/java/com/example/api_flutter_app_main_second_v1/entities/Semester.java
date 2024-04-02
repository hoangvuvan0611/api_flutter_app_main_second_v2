package com.example.api_flutter_app_main_second_v1.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class Semester {
    private String semesterName;
    private double gpa;
    private int totalCredit;
}
