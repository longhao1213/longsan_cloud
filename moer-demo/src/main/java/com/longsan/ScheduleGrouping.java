package com.longsan;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScheduleGrouping {
    public static void main(String[] args) {
        List<ScheduleInfo> reply = Arrays.asList(
            new ScheduleInfo(LocalDate.of(2024, 1, 1), "1"),
            new ScheduleInfo(LocalDate.of(2024, 1, 1), "2"),
            new ScheduleInfo(LocalDate.of(2024, 1, 2), "1"),
            new ScheduleInfo(LocalDate.of(2024, 1, 1), "1"), // 重复
            new ScheduleInfo(LocalDate.of(2024, 1, 2), "3")
        );

        Map<LocalDate, Set<ScheduleInfo>> groupedByDate = reply.stream()
            .collect(Collectors.groupingBy(ScheduleInfo::getDate,
                    Collectors.mapping(Function.identity(),
                            Collectors.toSet())));

        groupedByDate.forEach((date, scheduleInfos) -> {
            System.out.println("Date: " + date);
            scheduleInfos.forEach(scheduleInfo -> 
                System.out.println("    ID: " + scheduleInfo.getId()));
        });
    }
}
