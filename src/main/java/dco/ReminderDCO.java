package main.java.dco;

import java.time.LocalDateTime;

public record ReminderDCO(Integer taskId, LocalDateTime remindAt, String message) {}
