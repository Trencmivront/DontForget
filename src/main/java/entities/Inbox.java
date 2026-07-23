package main.java.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "INBOX")
public class Inbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long inboxId;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(updatable = false, nullable = false)
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

    // No-arg constructor
    public Inbox() {}

    // All-args constructor
    public Inbox(Long inboxId, String message, Timestamp createdAt) {
        this.inboxId = inboxId;
        this.message = message;
        this.createdAt = createdAt;
    }

	public Long getInboxId() {
		return inboxId;
	}

	public void setInboxId(Long inboxId) {
		this.inboxId = inboxId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

}
