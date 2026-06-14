package app.entities;

import java.sql.Date;

public record Task(
	Integer task_id,
	String task_title,
	String description,
	Integer status_id,
	Integer priority,
	Date due_date,
	Integer list_order,
	Integer project_id,
	Date created_at,
	Date updated_at,
	Date completed_at
) {}
