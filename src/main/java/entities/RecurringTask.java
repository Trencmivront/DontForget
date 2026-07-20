package main.java.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "RECURRING_TASK")
@IdClass(RecurringTask.RecurringTaskId.class)
public class RecurringTask {

    @Id
    @Column
    private Long taskId;

    @Id
    @Column
    private Long weekDayId;

    // No-arg constructor
    public RecurringTask() {}

    // All-args constructor
    public RecurringTask(Long taskId, Long weekDayId) {
        this.taskId = taskId;
        this.weekDayId = weekDayId;
    }

    // Record-like getters
    public Long taskId() { return taskId; }
    public Long weekDayId() { return weekDayId; }

    // Standard getters/setters
    public Long gettaskId() { return taskId; }
    public void settaskId(Long taskId) { this.taskId = taskId; }

    public Long getweekDayId() { return weekDayId; }
    public void setweekDayId(Long weekDayId) { this.weekDayId = weekDayId; }

    public static class RecurringTaskId implements Serializable {
        private static final long serialVersionUID = 1L;
		private Long taskId;
        private Long weekDayId;

        public RecurringTaskId() {}

        public RecurringTaskId(Long taskId, Long weekDayId) {
            this.taskId = taskId;
            this.weekDayId = weekDayId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RecurringTaskId that = (RecurringTaskId) o;
            return Objects.equals(taskId, that.taskId) && Objects.equals(weekDayId, that.weekDayId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(taskId, weekDayId);
        }
    }
}
