package main.java.services.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Task;

@Service
public class DeleteCompletedTasksService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteCompletedTasksService.class.getName());

	@Autowired
	private GetTasksOfProjectService getTasksOfProjectService;

	@Autowired
	private DeleteTaskService deleteTaskService;

	public DeleteCompletedTasksService(GetTasksOfProjectService getTasksOfProjectService, DeleteTaskService deleteTaskService) {
		this.getTasksOfProjectService = getTasksOfProjectService;
		this.deleteTaskService = deleteTaskService;
	}

	public ResponseEntity<String> execute(Long projectId) {
		logger.info("Executing {} for projectId: {}", this.getClass(), projectId);

		ResponseEntity<List<Task>> tasksResponse = getTasksOfProjectService.execute(projectId);
		List<Task> tasks = tasksResponse.getBody();
		if (tasks == null) {
			return ResponseEntity.notFound().build();
		}

		boolean success = true;
		for (Task task : tasks) {
			if (task.statusId() != null && task.statusId() == 2L) { // 2 = COMPLETED
				ResponseEntity<String> res = deleteTaskService.execute(task.taskId());
				if (res.getStatusCode().isError()) {
					success = false;
				}
			}
		}
		if (success) {
			return ResponseEntity.ok("COMPLETED TASKS DELETED");
		} else {
			return ResponseEntity.internalServerError().body("FAILED TO DELETE COMPLETED TASKS");
		}
	}
}
