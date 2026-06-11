package app.entities;

import java.time.LocalDateTime;

public record Reminder(Integer task_id, LocalDateTime remind_at, String cstm_message) {}
