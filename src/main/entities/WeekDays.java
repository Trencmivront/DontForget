package main.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "WEEK_DAYS")
public class WeekDays {
    @Id
    @Column(name = "week_day_id")
    private Long week_day_id;

    @Column(name = "day_name", nullable = false, length = 20)
    private String day_name;

    // No-arg constructor
    public WeekDays() {}

    // All-args constructor
    public WeekDays(Long week_day_id, String day_name) {
        this.week_day_id = week_day_id;
        this.day_name = day_name;
    }

    // Record-like getters
    public Long week_day_id() { return week_day_id; }
    public String day_name() { return day_name; }

    // Standard getters/setters
    public Long getWeek_day_id() { return week_day_id; }
    public void setWeek_day_id(Long week_day_id) { this.week_day_id = week_day_id; }

    public String getDay_name() { return day_name; }
    public void setDay_name(String day_name) { this.day_name = day_name; }
}
