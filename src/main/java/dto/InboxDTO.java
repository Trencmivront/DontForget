package main.java.dto;

import java.sql.Timestamp;
import main.java.entities.Inbox;

public class InboxDTO {

    private Long inboxId;
    private String message;
    private Timestamp createdAt;

    public InboxDTO() {}

    public InboxDTO(Long inboxId, String message, Timestamp createdAt) {
        this.inboxId = inboxId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public InboxDTO(Inbox inbox) {
        this.inboxId = inbox.getInboxId();
        this.message = inbox.getMessage();
        this.createdAt = inbox.getCreatedAt();
    }
//    for creating inbox
    public InboxDTO(String message) {
    	this.message = message;
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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
