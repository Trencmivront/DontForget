package main.java.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "WEEK_DAYS")
public class WeekDays {
    @Id
    @Column
    private Long weekDayId;

    @Column(nullable = false, length = 20)
    private String dayName;

    // No-arg constructor
    public WeekDays() {}

    // All-args constructor
    public WeekDays(Long weekDayId, String dayName) {
        this.weekDayId = weekDayId;
        this.dayName = dayName;
    }

    // Record-like getters
    public Long weekDayId() { return weekDayId; }
    public String dayName() { return dayName; }

    // Standard getters/setters
    public Long getweekDayId() { return weekDayId; }
    public void setweekDayId(Long weekDayId) { this.weekDayId = weekDayId; }

    public String getdayName() { return dayName; }
    public void setdayName(String dayName) { this.dayName = dayName; }
}
