package main.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.dto.TagDTO;
import main.java.services.tag.CreateTagService;
import main.java.services.tag.DeleteTagService;
import main.java.services.tag.GetTagService;
import main.java.services.tag.GetTagsOfTaskService;
import main.java.services.tag.GetTagsService;

@RestController
@RequestMapping("/api/tag")
public class TagController {

	private static final Logger logger = LoggerFactory.getLogger(TagController.class.getName());

	private final CreateTagService createTagService;
	private final DeleteTagService deleteTagService;
	private final GetTagService getTagService;
	private final GetTagsOfTaskService getTagsOfTaskService;
	private final GetTagsService getTagsService;

	public TagController(CreateTagService createTagService,
			DeleteTagService deleteTagService,
			GetTagService getTagService,
			GetTagsOfTaskService getTagsOfTaskService,
			GetTagsService getTagsService) {
		logger.info("Initializing TagController");
		this.createTagService = createTagService;
		this.deleteTagService = deleteTagService;
		this.getTagService = getTagService;
		this.getTagsOfTaskService = getTagsOfTaskService;
		this.getTagsService = getTagsService;
	}

	@PostMapping("/create")
	public ResponseEntity<Long> createTag(@RequestBody TagDTO tagDTO) {
		logger.info("Executing {} for tag: {}", this.getClass(), tagDTO);
		return createTagService.execute(tagDTO);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTag(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteTagService.execute(id);
	}

	@GetMapping("/get/{tagId}")
	public ResponseEntity<TagDTO> getTag(@PathVariable Long tagId) {
		logger.info("Executing {} for tagId: {}", this.getClass(), tagId);
		return getTagService.execute(tagId);
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<List<TagDTO>> getTagsOfTask(@PathVariable Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		return getTagsOfTaskService.execute(taskId);
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<TagDTO>> getTags() {
		logger.info("Executing {}", this.getClass());
		return getTagsService.execute(null);
	}
}
