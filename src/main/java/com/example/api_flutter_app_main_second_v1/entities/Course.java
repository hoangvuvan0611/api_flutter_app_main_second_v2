package com.example.api_flutter_app_main_second_v1.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Course {
    private String courseId;
    private String courseName;
    private String group;
    private int credit;
    private int tuitionFee;
    private String testStartDateTime;
    private String testEndDateTime;
    private String testAddress;
}
