package app.entities;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Reminder {
	private Integer task_id;
	private LocalDateTime remind_at;
	private String cstm_message;
}
