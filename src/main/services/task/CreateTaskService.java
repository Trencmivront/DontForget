package main.services.task;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dco.TaskDCO;
import main.entities.Task;
import main.repos.TaskRepository;

@Service
public class CreateTaskService {

	private static final Logger logger = Logger.getLogger(CreateTaskService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public Long execute(TaskDCO task) {
		logger.info("Executing CreateTaskService.");
		try {
			Long projectId = task.project_id();

			// Compute list_order as MAX + 1 for this project
			int listOrder = taskRepository.findMaxListOrderByProjectId(projectId) + 1;

			Task t = new Task();
			t.setTask_title(task.task_title());
			t.setDescription(task.description() == null || task.description().isEmpty() ? null : task.description());
			t.setStatus_id(Objects.requireNonNullElse(task.status_id(), 1L)); // 1 = ACTIVE
			t.setPriority(task.priority());
			t.setDue_date(task.due_date() != null ? Timestamp.valueOf(task.due_date().atStartOfDay()) : null);
			t.setList_order(listOrder);
			t.setProject_id(projectId);

			Task saved = taskRepository.save(t);
			logger.info("Task saved successfully.");
			return saved.task_id();
		} catch (Exception e) {
			logger.severe("Database error: " + e.getMessage());
			e.printStackTrace();
			return 0L;
		}
	}
}
