package com.example.api_flutter_app_main_second_v1.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class Score {
    private String scoreName;
    private char gpa;
    private double score;
    private int credit;
}
