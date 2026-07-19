package main.services.task;

import java.util.List;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Task;
import main.repos.TaskRepository;

@Service
public class GetTasksOfProjectService {

	private static final Logger logger = Logger.getLogger(GetTasksOfProjectService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> execute(Long id) {
		logger.info("Service executed.");
		try {
			return taskRepository.findByProject_id(id);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JDialog(), "Error while getting tasks for project id=" + id);
			e.printStackTrace();
			return null;
		}
	}
}
