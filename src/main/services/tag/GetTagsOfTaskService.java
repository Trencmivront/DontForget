package main.services.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Tag;
import main.entities.TaskTag;
import main.services.tasktag.GetTaskTagByTaskService;

@Service
public class GetTagsOfTaskService {

	private static final Logger logger = Logger.getLogger(GetTagsOfTaskService.class.getName());

	@Autowired
	private GetTaskTagByTaskService getTaskTagByTaskService;

	@Autowired
	private GetTagService getTagService;

	public List<Tag> execute(Long taskId) {
		logger.info("Executing GetTagsOfTaskService for taskId: " + taskId);

		List<Tag> tags = new ArrayList<>();
		List<TaskTag> taskTags = getTaskTagByTaskService.execute(taskId);

		if (taskTags != null) {
			for (TaskTag tt : taskTags) {
				Tag tag = getTagService.execute(tt.tag_id());
				if (tag != null) {
					tags.add(tag);
				}
			}
		}

		return tags;
	}
}
