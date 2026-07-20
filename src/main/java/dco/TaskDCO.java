package main.java.dco;

import java.time.LocalDate;

public record TaskDCO(
	String taskTitle,
	String description,
	Long statusId,
	Integer priority,
	LocalDate dueDate,
	Long projectId
) {}
