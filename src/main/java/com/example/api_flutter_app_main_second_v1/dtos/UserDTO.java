package com.example.api_flutter_app_main_second_v1.dtos;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDTO {
    private String userId;
    private String userName;
    private String dateOfBirth;
    private String classOfUser;
    private String department;
    private String specialized;
    private String tuitionFee;
    private String paidTuitionFee;
    private String gpa;
    private String totalCredit;
    private String currentSemester;
    private String dateStartSemester;
    private String isStudent;
    private List<CourseDTO> courseList;
    private List<SemesterDTO> semesterList;
}
