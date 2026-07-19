package main.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long task_id;

    @Column(name = "task_title", nullable = false, unique = true, length = 100)
    private String task_title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "status_id")
    private Long status_id;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "due_date")
    private Timestamp due_date;

    @Column(name = "list_order")
    private Integer list_order;

    @Column(name = "project_id")
    private Long project_id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updated_at;

    @Column(name = "completed_at")
    private Timestamp completed_at;

    // No-arg constructor
    public Task() {}

    // All-args constructor
    public Task(
        Long task_id,
        String task_title,
        String description,
        Long status_id,
        Integer priority,
        Timestamp due_date,
        Integer list_order,
        Long project_id,
        Timestamp created_at,
        Timestamp updated_at,
        Timestamp completed_at
    ) {
        this.task_id = task_id;
        this.task_title = task_title;
        this.description = description;
        this.status_id = status_id;
        this.priority = priority;
        this.due_date = due_date;
        this.list_order = list_order;
        this.project_id = project_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.completed_at = completed_at;
    }

    // Record-like getters
    public Long task_id() { return task_id; }
    public String task_title() { return task_title; }
    public String description() { return description; }
    public Long status_id() { return status_id; }
    public Integer priority() { return priority; }
    public Timestamp due_date() { return due_date; }
    public Integer list_order() { return list_order; }
    public Long project_id() { return project_id; }
    public Timestamp created_at() { return created_at; }
    public Timestamp updated_at() { return updated_at; }
    public Timestamp completed_at() { return completed_at; }

    // Standard getters/setters
    public Long getTask_id() { return task_id; }
    public void setTask_id(Long task_id) { this.task_id = task_id; }

    public String getTask_title() { return task_title; }
    public void setTask_title(String task_title) { this.task_title = task_title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getStatus_id() { return status_id; }
    public void setStatus_id(Long status_id) { this.status_id = status_id; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Timestamp getDue_date() { return due_date; }
    public void setDue_date(Timestamp due_date) { this.due_date = due_date; }

    public Integer getList_order() { return list_order; }
    public void setList_order(Integer list_order) { this.list_order = list_order; }

    public Long getProject_id() { return project_id; }
    public void setProject_id(Long project_id) { this.project_id = project_id; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

    public Timestamp getUpdated_at() { return updated_at; }
    public void setUpdated_at(Timestamp updated_at) { this.updated_at = updated_at; }

    public Timestamp getCompleted_at() { return completed_at; }
    public void setCompleted_at(Timestamp completed_at) { this.completed_at = completed_at; }
}
