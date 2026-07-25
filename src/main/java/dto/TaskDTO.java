package main.java.dto;

import java.time.LocalDate;
import java.sql.Timestamp;
import main.java.entities.Task;

public class TaskDTO {

    private Long taskId;
    private String taskTitle;
    private String description;
    private Long statusId;
    private Integer priority;
    private LocalDate dueDate;
    private Long projectId;

    public TaskDTO() {}

    public TaskDTO(Long taskId, String taskTitle, String description, Long statusId, Integer priority, LocalDate dueDate, Long projectId) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.description = description;
        this.statusId = statusId;
        this.priority = priority;
        this.dueDate = dueDate;
        this.projectId = projectId;
    }

    public TaskDTO(Task task) {
        this.taskId = task.getTaskId();
        this.taskTitle = task.getTaskTitle();
        this.description = task.getDescription();
        this.statusId = task.getStatusId();
        this.priority = task.getPriority();
        Timestamp ts = task.getDueDate();
        this.dueDate = ts != null ? ts.toLocalDateTime().toLocalDate() : null;
        this.projectId = task.getProjectId();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
