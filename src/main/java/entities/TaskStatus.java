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

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
