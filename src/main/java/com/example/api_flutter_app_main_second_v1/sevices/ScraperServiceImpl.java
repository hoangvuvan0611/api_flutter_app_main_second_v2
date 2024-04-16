package com.example.api_flutter_app_main_second_v1.sevices;

import com.example.api_flutter_app_main_second_v1.constants.DateTimeConstant;
import com.example.api_flutter_app_main_second_v1.dtos.*;
import com.example.api_flutter_app_main_second_v1.utils.date_time.MyDateTime;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.RouteMatcher;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ScraperServiceImpl implements ScraperService{

    @Value("${url.tuition}")
    private String tuitionUrl;
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
        return getDataUser(userCode);
    }

    private UserDTO getDataUser(String userCode){

        // SchedulePage
        Document document;
        try {
            document = Jsoup.connect(scheduleUrl + userCode).get();
        } catch (IOException e) {
            throw new RuntimeException("Schedule unService!");
        }

        if(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblContentMaSV") == null){
            return null;
        }

        String userId = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblContentMaSV").text().trim();

        String nameAndDateOfBirth = document
                .getElementById("ctl00_ContentPlaceHolder1_ctl00_lblContentTenSV").text().trim();
        String userName = nameAndDateOfBirth.substring(0, nameAndDateOfBirth.indexOf("-")).trim();
        String dateOfBirth = nameAndDateOfBirth.substring(nameAndDateOfBirth.indexOf(":") + 1).trim();

        String classAndDepartmentAndSpecialized = document
                .getElementById("ctl00_ContentPlaceHolder1_ctl00_lblContentLopSV").text().trim();
        String classOfUser = classAndDepartmentAndSpecialized
                .substring(0, classAndDepartmentAndSpecialized.indexOf("-")).trim();
        String department = classAndDepartmentAndSpecialized
                .substring(classAndDepartmentAndSpecialized.lastIndexOf(":") + 1).trim();
        String specialized = classAndDepartmentAndSpecialized
                .substring(classAndDepartmentAndSpecialized.indexOf(":") + 1,
                        classAndDepartmentAndSpecialized.lastIndexOf("-")).trim();

        String currentSemester = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK")
                .children().first().text().trim();
        String dateStartSemester = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblNote").text().trim();
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
            String tuition = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblphaiDong").text();
            tuition = tuition.replaceAll("\\s+", "");
            userDTO.setTuitionFee(tuition);

            tuition = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblDaDongHKOffline").text();
            tuition = tuition.replaceAll("\\s+", "");
            userDTO.setPaidTuitionFee(tuition);
        }

        // Score page
        try {
            document = Jsoup.connect(scoreSemesterUrl + userCode).get();
        } catch (IOException e) {
            throw new RuntimeException("Score unService!");
        }

        Elements elements = document.getElementsByClass("row-diemTK");
        String totalCredit = elements.get(elements.size() - 2).text();
        totalCredit = totalCredit.substring(totalCredit.indexOf(":") + 1).trim();
        String gpa = elements.get(elements.size() - 4).text();
        gpa = gpa.substring(gpa.indexOf(":") + 1).trim();

        userDTO.setTotalCredit(totalCredit);
        userDTO.setGpa(gpa);

        userDTO.setCourseList(getDataCourse(userDTO));
        userDTO.setSemesterList(getDataSemesterScore(userDTO));

        return userDTO;
    }

    @SneakyThrows
    private List<CourseDTO> getDataCourse(UserDTO user){
        List<CourseDTO> courseList = new ArrayList<>();
        CourseDTO course;
        // SchedulePage
        Document document;
        try {
            document = Jsoup.connect(scheduleUrl + user.getUserId()).get();
        } catch (IOException e) {
            throw new RuntimeException("Schedule unService!");
        }

        Elements elementsTable = document.getElementsByClass("grid-roll2").first().children();


        for(Element elementTable: elementsTable){
            Elements elementsTd = elementTable.child(0).child(0).children();
            course = CourseDTO.builder()
                .courseId(elementsTd.get(0).text().trim())
                .courseName(elementsTd.get(1).text().trim())
                .groupCode(elementsTd.get(2).text().trim())
                .credit(elementsTd.get(3).text().trim())
                .build();

            List<String> day = List.of(elementsTd.get(8).text().split(" "));
            List<String> startSlot = List.of(elementsTd.get(9).text().split(" "));
            List<String> sumSlot = List.of(elementsTd.get(10).text().split(" "));
            List<String> room = List.of(elementsTd.get(11).text().split(" "));
            List<String> time = List.of(elementsTd.get(13).text().split(" "));

            List<MeetingDTO> meetingList = new ArrayList<>();
            MeetingDTO meeting;
            for(byte i=0; i<time.size(); i++){
                List<Byte> listWeek = formatWeek(time.get(i));
                for (Byte aByte : listWeek) {
                    meeting = MeetingDTO.builder()
                        .startEndTime(
                            dateTimeFormat(
                                formatDay(day.get(i)),
                                aByte,
                                MyDateTime.convertStringToDate(
                                    user.getDateStartSemester(),
                                    DateTimeConstant.DATE_FORMAT
                                ),
                                startSlot.get(i),
                                sumSlot.get(i)
                            )
                        )
                        .roomName(room.get(i))
                        .build();
                    meeting.setCourse(course);
                    meetingList.add(meeting);
                }
            }
            course.setMeetingList(meetingList);
            courseList.add(course);
        }

        try {
            document = Jsoup.connect(testScheduleUrl + user.getUserId()).get();
        } catch (IOException e) {
            throw new RuntimeException("TestSchedule unService!");
        }

        String currentSemester = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_dropNHHK")
                .child(0).text().trim();
        if(currentSemester.equals(user.getCurrentSemester())){
            Elements elementsTr = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_gvXem")
                    .child(0).children();
            for(int i=1; i<elementsTr.size(); i++){
                Elements elementsTd = elementsTr.get(i).children();
                String courseCode = elementsTd.get(1).text().trim();
                for (CourseDTO courseDTO : courseList) {
                    if (courseCode.equals(courseDTO.getCourseId())) {
                        courseDTO.setTestRoom(elementsTd.get(9).text().trim());
                        String startTime = elementsTd.get(6).text().trim() + " " +
                                timeFormat(
                                        elementsTd.get(7).text().trim(),
                                        elementsTd.get(8).text().trim()
                                ).get(0);
                        String endTime = elementsTd.get(6).text().trim() + " " +
                                timeFormat(
                                        elementsTd.get(7).text().trim(),
                                        elementsTd.get(8).text().trim()
                                ).get(1);
                        courseDTO.setTestStartDateTime(startTime);
                        courseDTO.setTestEndDateTime(endTime);
                    }
                }
            }
        }

        try {
            document = Jsoup.connect(tuitionUrl + user.getUserId()).get();
        } catch (IOException e) {
            throw new RuntimeException("Tuition unService!");
        }

        currentSemester = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_lblNHHKOnline").text().trim();

        if(currentSemester.equals(user.getCurrentSemester())){
            Elements elementsTr = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_gvHocPhi")
                    .child(0).children();
            for(int i=1; i<elementsTr.size(); i++){
                Elements elementsTd = elementsTr.get(i).children();
                String courseCode = elementsTd.get(1).text().trim();
                for(CourseDTO courseDTO : courseList){
                    if(courseCode.equals(courseDTO.getCourseId())){
                        String tuition = elementsTd.get(7).text().trim();
                        tuition = tuition.replaceAll("\\s+", "");
                        courseDTO.setTuitionFee(tuition);
                    }
                }
            }
        }
        return courseList;
    }


    private List<SemesterDTO> getDataSemesterScore(UserDTO user){
        List<SemesterDTO> semesterList = new ArrayList<>();
        SemesterDTO semester;

        Document document;
        try {
            document = Jsoup.connect(scoreSemesterUrl + user.getUserId()).get();
        } catch (IOException e) {
            throw new RuntimeException("Schedule unService!");
        }

        Elements elementTable = document.getElementsByClass("view-table").first().child(0).children();

        List<Integer> semesterIndexList = new ArrayList<>();
        for(int i=0; i<elementTable.size(); i++){
            if(elementTable.get(i).hasClass("title-hk-diem")){
                semester = new SemesterDTO();
                semester.setSemesterName(elementTable.get(i).child(0).text().trim());
                List<ScoreDTO> scoreList = new ArrayList<>();
                for(int j=i+1; j<elementTable.size(); j++){
                    if(elementTable.get(j).hasClass("title-hk-diem")) {
                        i = j-1;
                        break;
                    } else {
                        if(elementTable.get(j).hasClass("row-diem")){
                            ScoreDTO score = new ScoreDTO();
                            if(!elementTable.get(j).child(3).text().trim().equals("0")){
                                score.setScoreName(elementTable.get(j).child(2).text().trim());
                                score.setCredit(elementTable.get(j).child(3).text().trim());
                                if(elementTable.get(j).child(10).text().trim().length() > 0){
                                    score.setScore(elementTable.get(j).child(10).text().trim());
                                    score.setGpa(elementTable.get(j).child(12).text().trim());
                                }
                                scoreList.add(score);
                            }
                        }else {
                            String inf = elementTable.get(j).child(0).text();
                            if(inf.contains("Điểm trung bình học kỳ hệ 4:")){
                                semester.setGpa(inf.substring(inf.indexOf(":") + 1).trim());
                            } else if(inf.contains("Số tín chỉ đạt:")){
                                semester.setTotalCredit(inf.substring(inf.indexOf(":") + 1).trim());
                            }
                        }
                    }
                }
                semester.setScoreList(scoreList);
                semester.setUser(user);
                semesterList.add(semester);
            }
        }
        return semesterList;
    }

    private List<Byte> formatWeek(String num){
        List<Byte> integerList = new ArrayList<>();
        for(byte i=0; i<num.length(); i++){
            if(num.charAt(i) == '-')
                continue;
            byte temp = (byte) (i+1);
            integerList.add(temp);
        }
        return integerList;
    }

    private Byte formatDay(String day){
        return switch (day) {
            case "Hai" -> 2;
            case "Ba" -> 3;
            case "Tư" -> 4;
            case "Năm" -> 5;
            case "Sáu" -> 6;
            case "Bảy" -> 7;
            case "CN" -> 1;
            default -> 0;
        };
    }

    private static List<String> dateTimeFormat(Byte day, Byte week, Date timeStartSemeter, String startSlot, String sumSlot){

        //Setup startDate of week
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeStartSemeter);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        if (day == 1) week++;

        calendar.add(Calendar.DAY_OF_WEEK, (week-1)*7);
        calendar.set(Calendar.DAY_OF_WEEK, day);


        List<Time> timeList = timeFormat(startSlot, sumSlot);

        Date sDate = calendar.getTime();
        sDate.setHours(timeList.get(0).getHours());
        sDate.setMinutes(timeList.get(0).getMinutes());


        Date eDate = calendar.getTime();
        eDate.setHours(timeList.get(1).getHours());
        eDate.setMinutes(timeList.get(1).getMinutes());

        List<String> dateList = new ArrayList<>();
        dateList.add(MyDateTime.convertDateToString(sDate, DateTimeConstant.DATE_TIME_FORMAT));
        dateList.add(MyDateTime.convertDateToString(eDate, DateTimeConstant.DATE_TIME_FORMAT));
        return dateList;
    }

    private static List<Time> timeFormat(String startSlot, String sumSlot){
        List<Time> listTime = new ArrayList<>();

        new Time(0);
        Time startTime = switch (startSlot) {
            case "2" -> Time.valueOf("07:55:00");
            case "3" -> Time.valueOf("08:50:00");
            case "4" -> Time.valueOf("09:55:00");
            case "5" -> Time.valueOf("10:50:00");
            case "6" -> Time.valueOf("12:45:00");
            case "7" -> Time.valueOf("13:40:00");
            case "8" -> Time.valueOf("14:35:00");
            case "9" -> Time.valueOf("15:40:00");
            case "10" -> Time.valueOf("16:35:00");
            case "11" -> Time.valueOf("18:00:00");
            case "12" -> Time.valueOf("18:55:00");
            case "13" -> Time.valueOf("19:50:00");
            default -> Time.valueOf("07:00:00");
        };

        listTime.add(startTime);

        LocalTime localTime = startTime.toLocalTime();
        long smSlot = Long.parseLong(sumSlot);
        localTime = localTime.plusMinutes(50*smSlot + (smSlot > 3 ? (5*(smSlot-1) + 15) : (5*(smSlot-1))));

        Time endTime = Time.valueOf(localTime);
        listTime.add(endTime);

        return listTime;
    }
}


