package main.dco;

import java.time.LocalDate;

public record TaskDCO(
	String task_title,
	String description,
	Integer status_id,
	Integer priority,
	LocalDate due_date,
	Integer project_id
) {}
