package main.io.github.trencmivront.dontforget.controllers;

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

import main.io.github.trencmivront.dontforget.dto.TaskTagDTO;
import main.io.github.trencmivront.dontforget.services.tasktag.CreateTaskTagService;
import main.io.github.trencmivront.dontforget.services.tasktag.DeleteTagsOfTaskService;
import main.io.github.trencmivront.dontforget.services.tasktag.DeleteTaskTagService;
import main.io.github.trencmivront.dontforget.services.tasktag.GetTaskTagByTaskService;

@RestController
@RequestMapping("/api/task-tag")
public class TaskTagController {

	private static final Logger logger = LoggerFactory.getLogger(TaskTagController.class.getName());

	private final CreateTaskTagService createTaskTagService;
	private final DeleteTaskTagService deleteTaskTagService;
	private final DeleteTagsOfTaskService deleteTagsOfTaskService;
	private final GetTaskTagByTaskService getTaskTagByTaskService;

	public TaskTagController(CreateTaskTagService createTaskTagService,
			DeleteTaskTagService deleteTaskTagService,
			DeleteTagsOfTaskService deleteTagsOfTaskService,
			GetTaskTagByTaskService getTaskTagByTaskService) {
		logger.info("Initializing TaskTagController");
		this.createTaskTagService = createTaskTagService;
		this.deleteTaskTagService = deleteTaskTagService;
		this.deleteTagsOfTaskService = deleteTagsOfTaskService;
		this.getTaskTagByTaskService = getTaskTagByTaskService;
	}

	@PostMapping("/create")
	public ResponseEntity<String> createTaskTag(@RequestBody TaskTagDTO taskTag) {
		logger.info("Executing {} for taskTag: {}", this.getClass(), taskTag);
		return createTaskTagService.execute(taskTag);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTaskTag(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteTaskTagService.execute(id);
	}

	@DeleteMapping("/delete/task/{taskId}")
	public ResponseEntity<String> deleteTagsOfTask(@PathVariable Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		return deleteTagsOfTaskService.execute(taskId);
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<List<TaskTagDTO>> getTaskTagByTask(@PathVariable Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		return getTaskTagByTaskService.execute(taskId);
	}
}
