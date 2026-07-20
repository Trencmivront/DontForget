package main.java.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "INBOX")
public class Inbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long inboxId;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(insertable = false, updatable = false)
    private Timestamp createdAt;

    // No-arg constructor
    public Inbox() {}

    // All-args constructor
    public Inbox(Long inboxId, String message, Timestamp createdAt) {
        this.inboxId = inboxId;
        this.message = message;
        this.createdAt = createdAt;
    }

    // Record-like getters
    public Long inboxId() { return inboxId; }
    public String message() { return message; }
    public Timestamp createdAt() { return createdAt; }

    // Standard getters/setters
    public Long getinboxId() { return inboxId; }
    public void setinboxId(Long inboxId) { this.inboxId = inboxId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getcreatedAt() { return createdAt; }
    public void setcreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
