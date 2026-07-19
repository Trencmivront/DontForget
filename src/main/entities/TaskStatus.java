package main.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "TASK_STATUS")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long status_id;

    @Column(name = "status_name", nullable = false, unique = true, length = 50)
    private String status_name;

    // No-arg constructor
    public TaskStatus() {}

    // All-args constructor
    public TaskStatus(Long status_id, String status_name) {
        this.status_id = status_id;
        this.status_name = status_name;
    }

    // Record-like getters
    public Long status_id() { return status_id; }
    public String status_name() { return status_name; }

    // Standard getters/setters
    public Long getStatus_id() { return status_id; }
    public void setStatus_id(Long status_id) { this.status_id = status_id; }

    public String getStatus_name() { return status_name; }
    public void setStatus_name(String status_name) { this.status_name = status_name; }
}
