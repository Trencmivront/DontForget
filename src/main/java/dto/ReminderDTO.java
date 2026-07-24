package main.java.dto;

import java.time.LocalDateTime;

public record ReminderDTO(Long taskId, LocalDateTime remindAt, String message) {}
