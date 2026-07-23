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
