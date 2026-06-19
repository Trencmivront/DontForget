package main.entities;

import java.sql.Timestamp;

public record Reminder(Long task_id, Timestamp remind_at, String cstm_message) {}
