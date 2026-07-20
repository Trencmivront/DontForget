package main.java.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "TASK_STATUS")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long statusId;

    @Column(nullable = false, unique = true, length = 50)
    private String statusName;

    // No-arg constructor
    public TaskStatus() {}

    // All-args constructor
    public TaskStatus(Long statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }

    // Record-like getters
    public Long statusId() { return statusId; }
    public String statusName() { return statusName; }

    // Standard getters/setters
    public Long getstatusId() { return statusId; }
    public void setstatusId(Long statusId) { this.statusId = statusId; }

    public String getstatusName() { return statusName; }
    public void setstatusName(String statusName) { this.statusName = statusName; }
}
