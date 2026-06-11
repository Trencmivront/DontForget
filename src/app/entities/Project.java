package app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Project {
	private Integer project_id;
	private String project_title;
	private Integer list_order;
	private Integer icon_color_id;
}
