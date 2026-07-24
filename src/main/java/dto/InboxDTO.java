package main.java.dto;

import java.sql.Timestamp;

public record InboxDTO(Long inboxId, String message, Timestamp createdAt) {}
