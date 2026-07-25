package main.java.dto;

import main.java.entities.TaskTag;

public class TaskTagDTO {

    private Long taskId;
    private Long tagId;

    public TaskTagDTO() {}

    public TaskTagDTO(Long taskId, Long tagId) {
        this.taskId = taskId;
        this.tagId = tagId;
    }

    public TaskTagDTO(TaskTag taskTag) {
        this.taskId = taskTag.getTaskId();
        this.tagId = taskTag.getTagId();
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
}
