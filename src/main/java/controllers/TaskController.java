package main.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.dco.TaskDCO;
import main.java.entities.Task;
import main.java.services.task.CreateTaskService;
import main.java.services.task.DeleteCompletedTasksService;
import main.java.services.task.DeleteTaskService;
import main.java.services.task.GetTaskByIdService;
import main.java.services.task.GetTasksOfProjectService;
import main.java.services.task.GetTasksService;
import main.java.services.task.GetTodaysTasksService;
import main.java.services.task.UpdateTaskService;

@RestController
@RequestMapping("/api/task")
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class.getName());
	
	private final CreateTaskService createTaskService;
	private final DeleteCompletedTasksService deleteCompletedTasksService;
	private final DeleteTaskService deleteTaskService;
	private final GetTaskByIdService getTaskByIdService;
	private final GetTasksOfProjectService getTasksOfProjectService;
	private final GetTasksService getTasksService;
	private final GetTodaysTasksService getTodaysTasksService;
	private final UpdateTaskService updateTaskService;
	
	public TaskController(CreateTaskService createTaskService, DeleteCompletedTasksService deleteCompletedTasksService,
			DeleteTaskService deleteTaskService, GetTaskByIdService getTaskByIdService,
			GetTasksOfProjectService getTasksOfProjectService, GetTasksService getTasksService,
			GetTodaysTasksService getTodaysTasksService, UpdateTaskService updateTaskService) {
		logger.info("Initializing TaskController");
		super();
		this.createTaskService = createTaskService;
		this.deleteCompletedTasksService = deleteCompletedTasksService;
		this.deleteTaskService = deleteTaskService;
		this.getTaskByIdService = getTaskByIdService;
		this.getTasksOfProjectService = getTasksOfProjectService;
		this.getTasksService = getTasksService;
		this.getTodaysTasksService = getTodaysTasksService;
		this.updateTaskService = updateTaskService;
	}

	@PostMapping("/create")
	public ResponseEntity<Long> createTask(@RequestBody TaskDCO task) {
		logger.info("Executing {} for task: {}", this.getClass(), task);
		return createTaskService.execute(task);
	}

	@DeleteMapping("/delete-completed/{projectId}")
	public ResponseEntity<String> deleteCompletedTasks(@PathVariable Long projectId) {
		logger.info("Executing {} for projectId: {}", this.getClass(), projectId);
		return deleteCompletedTasksService.execute(projectId);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTask(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteTaskService.execute(id);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return getTaskByIdService.execute(id);
	}

	@GetMapping("/project/{id}")
	public ResponseEntity<List<Task>> getTasksOfProject(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return getTasksOfProjectService.execute(id);
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<Task>> getTasks() {
		logger.info("Executing {}", this.getClass());
		return getTasksService.execute();
	}

	@GetMapping("/today")
	public ResponseEntity<List<Task>> getTodaysTasks() {
		logger.info("Executing {}", this.getClass());
		return getTodaysTasksService.execute();
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateTask(@RequestBody Task task) {
		logger.info("Executing {} for task: {}", this.getClass(), task);
		return updateTaskService.execute(task);
	}
}
