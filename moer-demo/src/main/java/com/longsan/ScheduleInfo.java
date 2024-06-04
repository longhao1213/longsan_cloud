package com.longsan;

import java.time.LocalDate;
import java.util.Objects;

public class ScheduleInfo {
    private LocalDate date;
    private String id;

    public ScheduleInfo(LocalDate date, String id) {
        this.date = date;
        this.id = id;
    }

    // 省略getter和setter方法
    public LocalDate getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    // 根据需求重写hashCode和equals方法，确保可以正确比较id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleInfo that = (ScheduleInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
