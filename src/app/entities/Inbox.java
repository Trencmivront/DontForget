package app.entities;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Inbox {
	private Integer inbox_id;
	private String message;
	private LocalDateTime created_at;
}
