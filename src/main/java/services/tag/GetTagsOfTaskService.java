package main.java.services.tag;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Tag;
import main.java.entities.TaskTag;
import main.java.services.tasktag.GetTaskTagByTaskService;

@Service
public class GetTagsOfTaskService {

	private static final Logger logger = LoggerFactory.getLogger(GetTagsOfTaskService.class.getName());

	@Autowired
	private GetTaskTagByTaskService getTaskTagByTaskService;

	@Autowired
	private GetTagService getTagService;

	public GetTagsOfTaskService(GetTaskTagByTaskService getTaskTagByTaskService, GetTagService getTagService) {
		this.getTaskTagByTaskService = getTaskTagByTaskService;
		this.getTagService = getTagService;
	}

	public ResponseEntity<List<Tag>> execute(Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);

		List<Tag> tags = new ArrayList<>();
		ResponseEntity<List<TaskTag>> taskTagsResponse = getTaskTagByTaskService.execute(taskId);
		List<TaskTag> taskTags = taskTagsResponse.getBody();

		if (taskTags != null) {
			for (TaskTag tt : taskTags) {
				ResponseEntity<Tag> tagResponse = getTagService.execute(tt.tagId());
				Tag tag = tagResponse.getBody();
				if (tag != null) {
					tags.add(tag);
				}
			}
		}

		return ResponseEntity.ok(tags);
	}
}
