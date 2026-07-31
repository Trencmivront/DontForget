package main.java.dto;

import java.time.LocalDateTime;
import main.java.entities.Reminder;

public class ReminderDTO {

    private Long taskId;
    private LocalDateTime remindAt;
    private String message;

    public ReminderDTO() {}

    public ReminderDTO(Long taskId, LocalDateTime remindAt, String message) {
        this.taskId = taskId;
        this.remindAt = remindAt;
        this.message = message;
    }

    public ReminderDTO(Reminder reminder) {
        this.taskId = reminder.getTaskId();
        this.remindAt = reminder.getRemindAt() != null ? reminder.getRemindAt().toLocalDateTime() : null;
        this.message = reminder.getMessage();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(LocalDateTime remindAt) {
        this.remindAt = remindAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReminderDTO)) return false;
        ReminderDTO other = (ReminderDTO) o;
        return java.util.Objects.equals(taskId, other.taskId)
            && java.util.Objects.equals(remindAt, other.remindAt)
            && java.util.Objects.equals(message, other.message);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(taskId, remindAt, message);
    }
}
