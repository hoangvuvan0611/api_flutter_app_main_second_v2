package com.example.api_flutter_app_main_second_v1.entities;

import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Setter
@Getter
public class Meeting {
    private String roomName;
    private Date dateTimeStart;
    private Date dateTimeEnd;
}
