package com.yachaerang.yachaerangbatch.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;

/*
ISO 국제 표준 주차 기준
 */
public class WeekUtils {

    /**
     * 특정 날짜가 속한 ISO 주차의 시작일(월요일) 반환
     */
    public static LocalDate getWeekStartDate(int year, int week) {
        return LocalDate.of(year, 1, 4)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                .with(DayOfWeek.MONDAY);
    }

    /**
     * 특정 날짜가 속한 ISO 주차의 종료일(일요일) 반환
     */
    public static LocalDate getWeekEndDate(int year, int week) {
        return getWeekStartDate(year, week).plusDays(6);
    }

    /**
     * 특정 날짜가 ISO 주차 몇번째인지를 반환
     */
    public static int[] getYearAndWeek(LocalDate date) {
        int year = date.get(IsoFields.WEEK_BASED_YEAR);
        int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return new int[]{year, week};
    }

    /**
     * 특정 년도의 ISO 주차의 마지막 주차 반환(52나 53)
     */
    public static int getLastIsoWeekOfYear(int year) {
        LocalDate dec28 = LocalDate.of(year, 12, 28);
        return dec28.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    public static int isoWeekOfYear(LocalDate date) {
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    public static int isoWeekBasedYear(LocalDate date) {
        return date.get(IsoFields.WEEK_BASED_YEAR);
    }
}
