package com.example.api_flutter_app_main_second_v1.sevices;

import com.example.api_flutter_app_main_second_v1.dtos.UserDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScraperServiceImpl implements ScraperService{

    @Value("${url.tuition}")
    private String tuitionUrl;
    @Value("${url.course}")
    private String courseUrl;
    @Value("${url.schedule}")
    private String scheduleUrl;
    @Value("${url.test_schedule}")
    private String testScheduleUrl;
    @Value("${url.score_semester}")
    private String scoreSemesterUrl;

    @Override
    public UserDTO scrappingData(String userCode){
        return scrappingUserTuition(userCode);
    }


    private UserDTO scrappingUserTuition(String userCode){
        UserDTO userDTO = getDataUser(userCode);


        return userDTO;
    }

    private UserDTO getDataUser(String userCode){

        // SchedulePage
        Document document = null;
        try {
            document = Jsoup.connect(scheduleUrl + userCode).get();
        } catch (IOException e) {
            throw new RuntimeException("Schedule unService!");
        }

        String userId = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblContentMaSV").text().trim();

        String nameAndDateOfBirth = document
                .getElementById("ctl00_ContentPlaceHolder1_ctl00_lblContentTenSV").text();
        String userName = nameAndDateOfBirth.substring(0, nameAndDateOfBirth.indexOf("-")).trim();
        String dateOfBirth = nameAndDateOfBirth.substring(nameAndDateOfBirth.indexOf(":") + 1).trim();

        String classAndDepartmentAndSpecialized = document
                .getElementById("ctl00_ContentPlaceHolder1_ctl00_lblContentLopSV").text();
        String classOfUser = classAndDepartmentAndSpecialized
                .substring(0, classAndDepartmentAndSpecialized.indexOf("-")).trim();
        String department = classAndDepartmentAndSpecialized
                .substring(classAndDepartmentAndSpecialized.lastIndexOf(":") + 1).trim();
        String specialized = classAndDepartmentAndSpecialized
                .substring(classAndDepartmentAndSpecialized.indexOf(":") + 1,
                        classAndDepartmentAndSpecialized.lastIndexOf("-")).trim();

        String currentSemester = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK")
                .children().first().text().trim();
        String dateStartSemester = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblNote").text();
        dateStartSemester = dateStartSemester
                .substring(dateStartSemester.lastIndexOf(")") - 10, dateStartSemester.length() - 1).trim();

        UserDTO userDTO = UserDTO.builder()
                .userId(userId)
                .userName(userName)
                .dateOfBirth(dateOfBirth)
                .classOfUser(classOfUser)
                .department(department)
                .specialized(specialized)
                .currentSemester(currentSemester)
                .dateStartSemester(dateStartSemester)
                .build();

        // Tuition page
        try {
            document = Jsoup.connect(tuitionUrl + userCode).get();
        } catch (IOException e) {
            throw new RuntimeException("Tuition unService!");
        }

        String currentSemesterTuitionPage = document
                .getElementById("ctl00_ContentPlaceHolder1_ctl00_lblNHHKOnline").text();

        if(currentSemester.equals(currentSemesterTuitionPage)){
            userDTO.setTotalCredit(
                    document.getElementById("ctl00_ContentPlaceHolder1_ctl00_SoTinChiHP").text());
            userDTO.setTuitionFee(
                    document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblphaiDong").text());
            userDTO.setPaidTuitionFee(
                    document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblDaDongHKOffline").text());
        }

        return userDTO;
    }


}


