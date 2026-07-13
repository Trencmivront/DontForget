package main.services.task;

import java.util.List;
import java.util.logging.Logger;

import main.entities.Task;

public class DeleteCompletedTasksService {


	private static final Logger logger = Logger.getLogger(DeleteCompletedTasksService.class.getName());

	public boolean execute(Long projectId) {
		logger.info("Class " + logger.getName() + " is executed with project id: " + projectId);

		List<Task> tasks = new GetTasksOfProjectService().execute(projectId);
		if (tasks == null) {
			return false;
		}

		boolean success = true;
		for (Task task : tasks) {
			if (task.status_id() != null && task.status_id() == 2L) { // 2 = COMPLETED
				if (!new DeleteTaskService().execute(task.task_id())) {
					success = false;
				}
			}
		}
		return success;
	}
}
