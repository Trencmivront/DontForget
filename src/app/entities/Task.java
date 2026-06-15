package app.entities;

import java.sql.Timestamp;

public record Task(
	Integer task_id,
	String task_title,
	String description,
	Integer status_id,
	Integer priority,
	Timestamp due_date,
	Integer list_order,
	Integer project_id,
	Timestamp created_at,
	Timestamp updated_at,
	Timestamp completed_at
) {}
