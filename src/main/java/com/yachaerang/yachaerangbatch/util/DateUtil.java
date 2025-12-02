package com.yachaerang.yachaerangbatch.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DateUtil {

    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern(("yyyy-MM-dd"));

    /*
    OPEN API 응답과 동일한 형식으로 반환
     */
    public static String formatForApi(LocalDate date) {
        return date.format(API_DATE_FORMAT);
    }

    /*
    String 형식의 날짜를 실제 날짜로 반환
     */
    public static LocalDate parseApiDate(String date) {
        return LocalDate.parse(date, API_DATE_FORMAT);
    }

    /*
    시작부터 종료까지의 일자들 반환
     */
    public static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            dateList.add(current);
        }
        return dateList;
    }

    /*
    이미 존재하는 날짜를 제외한 날짜 목록(List)을 반환
     */
    public static List<LocalDate> getMissingDates(LocalDate startDate, LocalDate endDate, List<LocalDate> existingDates) {
        Set<LocalDate> existingDateSet = existingDates.stream()
                .collect(Collectors.toSet());
        return getDateRange(startDate, endDate).stream()
                .filter(date -> !existingDateSet.contains(date))
                .collect(Collectors.toList());
    }
}
