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

    // Record-like getters
    public Long taskId() { return taskId; }
    public Timestamp remindAt() { return remindAt; }
    public String message() { return message; }

    // Standard getters/setters
    public Long gettaskId() { return taskId; }
    public void settaskId(Long taskId) { this.taskId = taskId; }

    public Timestamp getremindAt() { return remindAt; }
    public void setremindAt(Timestamp remindAt) { this.remindAt = remindAt; }

    public String getmessage() { return message; }
    public void setmessage(String message) { this.message = message; }
}
