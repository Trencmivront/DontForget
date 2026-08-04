package main.io.github.trencmivront.dontforget.services.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.TaskDTO;
import main.io.github.trencmivront.dontforget.entities.Task;
import main.io.github.trencmivront.dontforget.inter.Query;
import main.io.github.trencmivront.dontforget.repos.TaskRepository;

@Service
public class GetTasksOfProjectService implements Query<Long, List<TaskDTO>>{

	private static final Logger logger = LoggerFactory.getLogger(GetTasksOfProjectService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public GetTasksOfProjectService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<List<TaskDTO>> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			List<Task> tasks = taskRepository.findByprojectId(id);
			if(tasks == null || tasks.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			List<TaskDTO> dtos = tasks.stream()
				.map(TaskDTO::new)
				.toList();
			
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JDialog(), "Error while getting tasks for project id=" + id);
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
