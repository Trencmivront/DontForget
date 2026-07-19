package main.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "REMINDER")
public class Reminder {
    @Id
    @Column(name = "task_id")
    private Long task_id;

    @Column(name = "remind_at", nullable = false)
    private Timestamp remind_at;

    @Column(name = "cstm_message", length = 1000)
    private String cstm_message;

    // No-arg constructor
    public Reminder() {}

    // All-args constructor
    public Reminder(Long task_id, Timestamp remind_at, String cstm_message) {
        this.task_id = task_id;
        this.remind_at = remind_at;
        this.cstm_message = cstm_message;
    }

    // Record-like getters
    public Long task_id() { return task_id; }
    public Timestamp remind_at() { return remind_at; }
    public String cstm_message() { return cstm_message; }

    // Standard getters/setters
    public Long getTask_id() { return task_id; }
    public void setTask_id(Long task_id) { this.task_id = task_id; }

    public Timestamp getRemind_at() { return remind_at; }
    public void setRemind_at(Timestamp remind_at) { this.remind_at = remind_at; }

    public String getCstm_message() { return cstm_message; }
    public void setCstm_message(String cstm_message) { this.cstm_message = cstm_message; }
}
