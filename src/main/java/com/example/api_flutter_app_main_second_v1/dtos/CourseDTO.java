package com.example.api_flutter_app_main_second_v1.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CourseDTO {
    private String courseId;
    private String courseName;
    private String groupCode;
    private String classCode;
    private String credit;
    private String tuitionFee;
    private String testStartDateTime;
    private String testEndDateTime;
    private String testRoom;
    private String lesson;
    @JsonIgnore
    private UserDTO user;
    private List<MeetingDTO> meetingList;
}
