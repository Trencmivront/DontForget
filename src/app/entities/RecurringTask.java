package app.entities;

import lombok.Data;

@Data
public class RecurringTask {
	private Integer task_id;
	private String day_of_week;
	private Integer max_occourrences;
}
