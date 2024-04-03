package com.example.api_flutter_app_main_second_v1.utils.date_time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateTime {
    public static Date convertStringToDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setLenient(false);     //setLenient có nghĩa là nếu để true thì khi input là 31/2 thì nó sẽ tự động hiểu là 3 ngày sau ngày 28 thì thành 3/3
        return simpleDateFormat.parse(dateStr);
    }
    public static String convertDateToString(Date date, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
}
