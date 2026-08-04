package main.io.github.trencmivront.dontforget.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "WEEK_DAYS")
public class WeekDays {
	
    @Id
    @Column
    private Long weekDayId;

    @Column(nullable = false, length = 10, unique = true)
    private String dayName;

    // No-arg constructor
    public WeekDays() {}

    // All-args constructort
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
