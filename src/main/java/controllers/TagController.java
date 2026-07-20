package main.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.entities.Tag;
import main.java.services.tag.DeleteTagService;
import main.java.services.tag.GetTagService;
import main.java.services.tag.GetTagsOfTaskService;
import main.java.services.tag.GetTagsService;

@RestController
@RequestMapping("/api/tag")
public class TagController {

	private static final Logger logger = LoggerFactory.getLogger(TagController.class.getName());

	private final DeleteTagService deleteTagService;
	private final GetTagService getTagService;
	private final GetTagsOfTaskService getTagsOfTaskService;
	private final GetTagsService getTagsService;

	public TagController(DeleteTagService deleteTagService,
			GetTagService getTagService,
			GetTagsOfTaskService getTagsOfTaskService,
			GetTagsService getTagsService) {
		logger.info("Initializing TagController");
		this.deleteTagService = deleteTagService;
		this.getTagService = getTagService;
		this.getTagsOfTaskService = getTagsOfTaskService;
		this.getTagsService = getTagsService;
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTag(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteTagService.execute(id);
	}

	@GetMapping("/get/{tagId}")
	public ResponseEntity<Tag> getTag(@PathVariable Long tagId) {
		logger.info("Executing {} for tagId: {}", this.getClass(), tagId);
		return getTagService.execute(tagId);
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<List<Tag>> getTagsOfTask(@PathVariable Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		return getTagsOfTaskService.execute(taskId);
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<Tag>> getTags() {
		logger.info("Executing {}", this.getClass());
		return getTagsService.execute();
	}
}
