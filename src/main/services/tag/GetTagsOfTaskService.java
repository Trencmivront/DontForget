package main.services.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.entities.Tag;
import main.entities.TaskTag;
import main.services.tasktag.GetTaskTagByTaskService;

public class GetTagsOfTaskService {

	private GetTagsOfTaskService() {}

	private static final Logger logger = Logger.getLogger(GetTagsOfTaskService.class.getName());

	public static List<Tag> execute(int taskId) {
		logger.info("Executing GetTagsOfTaskService for taskId: " + taskId);

		List<Tag> tags = new ArrayList<>();
		List<TaskTag> taskTags = GetTaskTagByTaskService.execute(taskId);

		if (taskTags != null) {
			for (TaskTag tt : taskTags) {
				Tag tag = GetTagService.execute(tt.tag_id());
				if (tag != null) {
					tags.add(tag);
				}
			}
		}

		return tags;
	}
}
