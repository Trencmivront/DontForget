package app.entities;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Task {
	private Integer task_id;
	private String task_title;
	private String description;
	private Integer status_id;
	private Integer priority;
	private LocalDate due_date;
	private Integer list_order;
	private Integer project_id;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private LocalDateTime completed_at;
}
