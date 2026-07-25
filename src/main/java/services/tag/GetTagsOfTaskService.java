package main.java.services.tag;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.TagDTO;
import main.java.dto.TaskTagDTO;
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

	public ResponseEntity<List<TagDTO>> execute(Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);

		List<TagDTO> tags = new ArrayList<>();
		ResponseEntity<List<TaskTagDTO>> taskTagsResponse = getTaskTagByTaskService.execute(taskId);
		List<TaskTagDTO> taskTags = taskTagsResponse.getBody();

		if (taskTags != null) {
			for (TaskTagDTO tt : taskTags) {
				ResponseEntity<TagDTO> tagResponse = getTagService.execute(tt.getTagId());
				TagDTO tag = tagResponse.getBody();
				if (tag != null) {
					tags.add(tag);
				}
			}
		}

		return ResponseEntity.ok(tags);
	}
}
