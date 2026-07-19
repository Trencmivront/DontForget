package main.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "RECURRING_TASK")
@IdClass(RecurringTask.RecurringTaskId.class)
public class RecurringTask {

    @Id
    @Column(name = "task_id")
    private Long task_id;

    @Id
    @Column(name = "week_day_id")
    private Long week_day_id;

    // No-arg constructor
    public RecurringTask() {}

    // All-args constructor
    public RecurringTask(Long task_id, Long week_day_id) {
        this.task_id = task_id;
        this.week_day_id = week_day_id;
    }

    // Record-like getters
    public Long task_id() { return task_id; }
    public Long week_day_id() { return week_day_id; }

    // Standard getters/setters
    public Long getTask_id() { return task_id; }
    public void setTask_id(Long task_id) { this.task_id = task_id; }

    public Long getWeek_day_id() { return week_day_id; }
    public void setWeek_day_id(Long week_day_id) { this.week_day_id = week_day_id; }

    public static class RecurringTaskId implements Serializable {
        private static final long serialVersionUID = 1L;
		private Long task_id;
        private Long week_day_id;

        public RecurringTaskId() {}

        public RecurringTaskId(Long task_id, Long week_day_id) {
            this.task_id = task_id;
            this.week_day_id = week_day_id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RecurringTaskId that = (RecurringTaskId) o;
            return Objects.equals(task_id, that.task_id) && Objects.equals(week_day_id, that.week_day_id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task_id, week_day_id);
        }
    }
}
