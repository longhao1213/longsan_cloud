package com.longsan;

import com.nlf.calendar.Holiday;
import com.nlf.calendar.util.HolidayUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestSchedule {

    // 法定休假日
    static List<LocalDate> holidays = new ArrayList<>();
    // 调休日
    static List<LocalDate> holidayWorkDay = new ArrayList<>();

    static {
        for (Holiday holiday : HolidayUtil.getHolidays(LocalDate.now().getYear())) {
            if (holiday.isWork()) {
                holidayWorkDay.add(LocalDate.parse(holiday.getDay()));
            } else {
                holidays.add(LocalDate.parse(holiday.getDay()));
            }
        }
    }

    public static void main(String[] args) {
        //  设置工作日
        Set<DayOfWeek> workDay = new HashSet<>();
        workDay.add(DayOfWeek.MONDAY);
        workDay.add(DayOfWeek.WEDNESDAY);
        workDay.add(DayOfWeek.FRIDAY);
//        workDay.add(DayOfWeek.SUNDAY);

        LocalDate now = LocalDate.now();
        // 获取当前周的星期天
        LocalDate thisSunday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        // 下周的周一
        LocalDate nextMonday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        // 下周周日
        LocalDate nextSunday = nextMonday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        int workDayCount = getWorkDayCount(now, thisSunday, workDay);
        int nextWorkDayCount = getWorkDayCount(nextMonday, nextSunday, workDay);
        System.out.println(workDayCount);
    }

    /**
     *
     * @param start
     * @param end
     * @param workDay 工作日
     * @return
     */
    public static int getWorkDayCount(LocalDate start, LocalDate end, Set<DayOfWeek> workDay) {
        List<LocalDate> needWorkDays = new ArrayList<>();
        // 判断这周里面是否有节假日和调休
        List<LocalDate> weekDays = new ArrayList<>();
        LocalDate tempTime = start;
        // 本周因为法定节假日休息的时间
        int weekHoliday = 0;
        // 本周已经补班的时间
        int weekWorkDay = 0;
        while (!tempTime.isAfter(end)) {
            // 判断当前时间是否是工作日,并且不是法定节假日
            if (workDay.contains(tempTime.getDayOfWeek()) && !holidays.contains(tempTime)) {
                needWorkDays.add(tempTime);
            }
            // 如果是法定节假日休息，那么计数一次
            if (holidays.contains(tempTime)) {
                weekHoliday++;
            }
            // 如果是调休日，那么判断这周的休息日，是否大于补班日
            if (holidayWorkDay.contains(tempTime) && weekHoliday > weekWorkDay) {
                needWorkDays.add(tempTime);
            }

            tempTime = tempTime.plusDays(1);
        }
        return needWorkDays.size();
    }

}
