package main.java.dto;

import main.java.entities.TaskStatus;

public class TaskStatusDTO {

    private Long taskStatusId;
    private String statusName;

    public TaskStatusDTO() {}

    public TaskStatusDTO(Long taskStatusId, String statusName) {
        this.taskStatusId = taskStatusId;
        this.statusName = statusName;
    }

    public TaskStatusDTO(TaskStatus taskStatus) {
        this.taskStatusId = taskStatus.getStatusId();
        this.statusName = taskStatus.getStatusName();
    }

    public Long getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(Long taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
