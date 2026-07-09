package main.entities;

import java.sql.Timestamp;

public record Task(
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
) {}
