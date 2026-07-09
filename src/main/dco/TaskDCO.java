package main.dco;

import java.time.LocalDate;

public record TaskDCO(
	String task_title,
	String description,
	Long status_id,
	Integer priority,
	LocalDate due_date,
	Long project_id
) {}
