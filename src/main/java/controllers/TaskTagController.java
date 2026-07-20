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

import main.java.entities.TaskTag;
import main.java.services.tasktag.CreateTaskTagService;
import main.java.services.tasktag.DeleteTaskTagService;
import main.java.services.tasktag.GetTaskTagByTaskService;

@RestController
@RequestMapping("/api/task-tag")
public class TaskTagController {

	private static final Logger logger = LoggerFactory.getLogger(TaskTagController.class.getName());

	private final CreateTaskTagService createTaskTagService;
	private final DeleteTaskTagService deleteTaskTagService;
	private final GetTaskTagByTaskService getTaskTagByTaskService;

	public TaskTagController(CreateTaskTagService createTaskTagService,
			DeleteTaskTagService deleteTaskTagService,
			GetTaskTagByTaskService getTaskTagByTaskService) {
		logger.info("Initializing TaskTagController");
		this.createTaskTagService = createTaskTagService;
		this.deleteTaskTagService = deleteTaskTagService;
		this.getTaskTagByTaskService = getTaskTagByTaskService;
	}

	@PostMapping("/create")
	public ResponseEntity<String> createTaskTag(@RequestBody TaskTag taskTag) {
		logger.info("Executing {} for taskTag: {}", this.getClass(), taskTag);
		return createTaskTagService.execute(taskTag);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTaskTag(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteTaskTagService.execute(id);
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<List<TaskTag>> getTaskTagByTask(@PathVariable Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		return getTaskTagByTaskService.execute(taskId);
	}
}
