package main.java.dto;

import java.time.LocalDate;

public record TaskDTO(
	Long taskId,
	String taskTitle,
	String description,
	Long statusId,
	Integer priority,
	LocalDate dueDate,
	Long projectId
) {}
