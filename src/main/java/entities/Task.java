package main.java.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long taskId;

    @Column(nullable = false, unique = true, length = 100)
    private String taskTitle;

    @Column(length = 1000)
    private String description;

    @Column
    private Long statusId;

    @Column
    private Integer priority;

    @Column
    private Timestamp dueDate;

    @Column
    private Integer listOrder;

    @Column
    private Long projectId;

    @Column(insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    private Timestamp updatedAt;

    @Column
    private Timestamp completedAt;

    // No-arg constructor
    public Task() {}

    // All-args constructor
    public Task(
        Long taskId,
        String taskTitle,
        String description,
        Long statusId,
        Integer priority,
        Timestamp dueDate,
        Integer listOrder,
        Long projectId,
        Timestamp createdAt,
        Timestamp updatedAt,
        Timestamp completedAt
    ) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.description = description;
        this.statusId = statusId;
        this.priority = priority;
        this.dueDate = dueDate;
        this.listOrder = listOrder;
        this.projectId = projectId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }

    // Record-like getters
    public Long taskId() { return taskId; }
    public String taskTitle() { return taskTitle; }
    public String description() { return description; }
    public Long statusId() { return statusId; }
    public Integer priority() { return priority; }
    public Timestamp dueDate() { return dueDate; }
    public Integer listOrder() { return listOrder; }
    public Long projectId() { return projectId; }
    public Timestamp createdAt() { return createdAt; }
    public Timestamp updatedAt() { return updatedAt; }
    public Timestamp completedAt() { return completedAt; }

    // Standard getters/setters
    public Long gettaskId() { return taskId; }
    public void settaskId(Long taskId) { this.taskId = taskId; }

    public String gettaskTitle() { return taskTitle; }
    public void settaskTitle(String taskTitle) { this.taskTitle = taskTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getstatusId() { return statusId; }
    public void setstatusId(Long statusId) { this.statusId = statusId; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Timestamp getdueDate() { return dueDate; }
    public void setdueDate(Timestamp dueDate) { this.dueDate = dueDate; }

    public Integer getlistOrder() { return listOrder; }
    public void setlistOrder(Integer listOrder) { this.listOrder = listOrder; }

    public Long getprojectId() { return projectId; }
    public void setprojectId(Long projectId) { this.projectId = projectId; }

    public Timestamp getcreatedAt() { return createdAt; }
    public void setcreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getupdatedAt() { return updatedAt; }
    public void setupdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getcompletedAt() { return completedAt; }
    public void setcompletedAt(Timestamp completedAt) { this.completedAt = completedAt; }
}
