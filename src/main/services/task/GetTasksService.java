package main.services.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Task;
import main.repos.TaskRepository;

@Service
public class GetTasksService {

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> execute() {
		List<Task> tasks = taskRepository.findAll();
		return tasks.isEmpty() ? null : tasks;
	}
}
