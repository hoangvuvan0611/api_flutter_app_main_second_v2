package com.example.api_flutter_app_main_second_v1.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MeetingDTO {
    private String roomName;
    private List<String> startEndTime;
    private String week;
    private String currentWeek;
    private String lesson;
    @JsonIgnore
    private CourseDTO course;
}
