package main.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TASK_TAG")
@IdClass(TaskTag.TaskTagId.class)
public class TaskTag {

    @Id
    @Column(name = "task_id")
    private Long task_id;

    @Id
    @Column(name = "tag_id")
    private Long tag_id;

    // No-arg constructor
    public TaskTag() {}

    // All-args constructor
    public TaskTag(Long task_id, Long tag_id) {
        this.task_id = task_id;
        this.tag_id = tag_id;
    }

    // Record-like getters
    public Long task_id() { return task_id; }
    public Long tag_id() { return tag_id; }

    // Standard getters/setters
    public Long getTask_id() { return task_id; }
    public void setTask_id(Long task_id) { this.task_id = task_id; }

    public Long getTag_id() { return tag_id; }
    public void setTag_id(Long tag_id) { this.tag_id = tag_id; }

    public static class TaskTagId implements Serializable {
        private Long task_id;
        private Long tag_id;

        public TaskTagId() {}

        public TaskTagId(Long task_id, Long tag_id) {
            this.task_id = task_id;
            this.tag_id = tag_id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TaskTagId taskTagId = (TaskTagId) o;
            return Objects.equals(task_id, taskTagId.task_id) && Objects.equals(tag_id, taskTagId.tag_id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task_id, tag_id);
        }
    }
}
