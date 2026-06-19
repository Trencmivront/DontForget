package main.dco;

import java.time.LocalDateTime;

public record ReminderDCO(Integer task_id, LocalDateTime remind_at, String cstm_message) {}
