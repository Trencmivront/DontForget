package app.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Task(
	Integer task_id,
	String task_title,
	String description,
	Integer status_id,
	Integer priority,
	LocalDate due_date,
	Integer list_order,
	Integer project_id,
	LocalDateTime created_at,
	LocalDateTime updated_at,
	LocalDateTime completed_at
) {}
