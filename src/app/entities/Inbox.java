package app.entities;

import java.time.LocalDateTime;

public record Inbox(Integer inbox_id, String message, LocalDateTime created_at) {}
