package main.io.github.trencmivront.dontforget.dto;

import main.io.github.trencmivront.dontforget.entities.WeekDays;

public class WeekDaysDTO {

    private Long weekDayId;
    private String dayName;

    public WeekDaysDTO() {}

    public WeekDaysDTO(Long weekDayId, String dayName) {
        this.weekDayId = weekDayId;
        this.dayName = dayName;
    }

    public WeekDaysDTO(WeekDays weekDays) {
        this.weekDayId = weekDays.getWeekDayId();
        this.dayName = weekDays.getDayName();
    }

    public Long getWeekDayId() {
        return weekDayId;
    }

    public void setWeekDayId(Long weekDayId) {
        this.weekDayId = weekDayId;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}
