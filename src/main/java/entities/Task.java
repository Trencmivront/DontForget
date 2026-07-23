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

	public Timestamp getDueDate() {
		return dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getListOrder() {
		return listOrder;
	}

	public void setListOrder(Integer listOrder) {
		this.listOrder = listOrder;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Timestamp getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Timestamp completedAt) {
		this.completedAt = completedAt;
	}
}
