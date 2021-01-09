package com.lens.epay.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Emir Gökdemir
 * on 1 May 2020
 */

public class DateUtil {

    public static ZonedDateTime stringToZonedDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ZonedDateTime.parse(dateTime, formatter);
    }

    public static ZonedDateTime stringToZonedDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ZonedDateTime.parse(date, formatter);
    }

}
