package main.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "INBOX")
public class Inbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inbox_id")
    private Long inbox_id;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp created_at;

    // No-arg constructor
    public Inbox() {}

    // All-args constructor
    public Inbox(Long inbox_id, String message, Timestamp created_at) {
        this.inbox_id = inbox_id;
        this.message = message;
        this.created_at = created_at;
    }

    // Record-like getters
    public Long inbox_id() { return inbox_id; }
    public String message() { return message; }
    public Timestamp created_at() { return created_at; }

    // Standard getters/setters
    public Long getInbox_id() { return inbox_id; }
    public void setInbox_id(Long inbox_id) { this.inbox_id = inbox_id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
}
