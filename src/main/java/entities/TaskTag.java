package main.java.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TASK_TAG")
@IdClass(TaskTag.TaskTagId.class)
public class TaskTag {

    @Id
    @Column
    private Long taskId;

    @Id
    @Column
    private Long tagId;

    // No-arg constructor
    public TaskTag() {}

    // All-args constructor
    public TaskTag(Long taskId, Long tagId) {
        this.taskId = taskId;
        this.tagId = tagId;
    }

    public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public static class TaskTagId implements Serializable {
        private static final long serialVersionUID = 1L;
		private Long taskId;
        private Long tagId;

        public TaskTagId() {}

        public TaskTagId(Long taskId, Long tagId) {
            this.taskId = taskId;
            this.tagId = tagId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TaskTagId taskTagId = (TaskTagId) o;
            return Objects.equals(taskId, taskTagId.taskId) && Objects.equals(tagId, taskTagId.tagId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(taskId, tagId);
        }
    }
}
