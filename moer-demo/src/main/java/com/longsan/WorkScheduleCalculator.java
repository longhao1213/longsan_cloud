package com.longsan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class WorkScheduleCalculator {
    private static int calculateMinimumWeeks(int totalUsers, Set<LocalDate> holidays, Set<LocalDate> extraWorkdays) {
        Set<DayOfWeek> workDays = new HashSet<>(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
        int weeksNeeded = 0;

        LocalDate currentDate = LocalDate.now(); // Start from today or the beginning of your scheduling period

        while (totalUsers > 0) {
            long weekOfYear = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

            LocalDate finalCurrentDate = currentDate;
            LocalDate finalCurrentDate1 = currentDate;
            long countWorkdaysThisWeek = workDays.stream()
                .filter(dayOfWeek -> !holidays.contains(finalCurrentDate.with(TemporalAdjusters.nextOrSame(dayOfWeek))) && !finalCurrentDate.with(TemporalAdjusters.nextOrSame(dayOfWeek)).isAfter(finalCurrentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))))
                .filter(dayOfWeek -> extraWorkdays.contains(finalCurrentDate1.with(TemporalAdjusters.nextOrSame(dayOfWeek))) || !holidays.contains(finalCurrentDate1.with(TemporalAdjusters.nextOrSame(dayOfWeek))))
                .count();

            totalUsers -= countWorkdaysThisWeek;
            weeksNeeded++;
            currentDate = currentDate.plusWeeks(1); // Move to next week
        }

        return weeksNeeded;
    }

    public static void main(String[] args) {
        int totalUsers = 100;
        Set<LocalDate> holidays = new HashSet<>(Arrays.asList(
            LocalDate.of(2024, 1, 1), // New Year's Day
            LocalDate.of(2024, 1, 2)  // Just an example
        ));
        Set<LocalDate> extraWorkdays = new HashSet<>(Arrays.asList(
            LocalDate.of(2024, 1, 6)  // An example extra workday
        ));

        int minimumWeeks = calculateMinimumWeeks(totalUsers, holidays, extraWorkdays);
        System.out.println("Minimum weeks required: " + minimumWeeks);
    }
}
