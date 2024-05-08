package com.risknarrative.springexercise.companysearch.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class DateUtil {

    /**
     * Converts a Date object to a string in the format YYYY-MM-DD.
     * @param date The Date object to convert.
     * @return A string representing the date in YYYY-MM-DD format.
     */
    public static String convertToStringDate(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * Converts a string in the format YYYY-MM-DD to a Date object using Java 8 API.
     * @param dateString The string to convert.
     * @return A Date object or null if the input string is invalid.
     */
    public static Date convertToJavaDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            log.info("@DateUtil: Invalid date format: " + dateString);
            return null;
        }
    }
}
