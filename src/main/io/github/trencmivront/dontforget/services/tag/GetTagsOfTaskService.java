package main.io.github.trencmivront.dontforget.services.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.TagDTO;
import main.io.github.trencmivront.dontforget.dto.TaskTagDTO;
import main.io.github.trencmivront.dontforget.inter.Query;
import main.io.github.trencmivront.dontforget.services.tasktag.GetTaskTagByTaskService;

@Service
public class GetTagsOfTaskService implements Query<Long, List<TagDTO>>{

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

		List<TagDTO> tags = new ArrayList<TagDTO>();
		ResponseEntity<List<TaskTagDTO>> taskTagsResponse = getTaskTagByTaskService.execute(taskId);
		List<TaskTagDTO> taskTags = taskTagsResponse.getBody();

		if (taskTags != null) {
			tags = taskTags.stream()
				    .map(tt -> getTagService.execute(tt.getTagId()).getBody())
				    .filter(Objects::nonNull).toList();
		}

		return ResponseEntity.ok(tags);
	}
}
