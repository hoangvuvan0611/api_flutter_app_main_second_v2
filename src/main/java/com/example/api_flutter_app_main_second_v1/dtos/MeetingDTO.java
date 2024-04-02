package com.example.api_flutter_app_main_second_v1.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MeetingDTO {
    private String roomName;
    private String dateTimeStart;
    private String dateTimeEnd;
    @JsonIgnore
    private CourseDTO course;
}
