package app.entities;

import java.sql.Timestamp;

public record Inbox(Long inbox_id, String message, Timestamp created_at) {}
