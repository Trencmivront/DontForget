package main.java.services.tasktag;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.TaskTagDTO;
import main.java.entities.TaskTag;
import main.java.repos.TaskTagRepository;

@Service
public class GetTaskTagByTaskService {

	private static final Logger logger = LoggerFactory.getLogger(GetTaskTagByTaskService.class.getName());

	@Autowired
	private TaskTagRepository taskTagRepository;

	public GetTaskTagByTaskService(TaskTagRepository taskTagRepository) {
		this.taskTagRepository = taskTagRepository;
	}

	public ResponseEntity<List<TaskTagDTO>> execute(Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		try {
			List<TaskTag> taskTags = taskTagRepository.findBytaskId(taskId);
			List<TaskTagDTO> dtos = taskTags.stream()
				.map(tt -> new TaskTagDTO(tt))
				.toList();
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			logger.warn("Error fetching task tags for task ID {}: {}", taskId, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
