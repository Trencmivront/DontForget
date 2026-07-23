package main.java.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "REMINDER")
public class Reminder {
    @Id
    @Column
    private Long taskId;

    @Column(nullable = false)
    private Timestamp remindAt;

    @Column(length = 1000)
    private String message;

    // No-arg constructor
    public Reminder() {}

    // All-args constructor
    public Reminder(Long taskId, Timestamp remindAt, String message) {
        this.taskId = taskId;
        this.remindAt = remindAt;
        this.message = message;
    }

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Timestamp getRemindAt() {
		return remindAt;
	}

	public void setRemindAt(Timestamp remindAt) {
		this.remindAt = remindAt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
