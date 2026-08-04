package main.io.github.trencmivront.dontforget.services.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.ProjectDTO;
import main.io.github.trencmivront.dontforget.entities.Project;
import main.io.github.trencmivront.dontforget.inter.Query;
import main.io.github.trencmivront.dontforget.repos.ProjectRepository;

@Service
public class GetProjectOfTaskService implements Query<Long, ProjectDTO>{

	private static final Logger logger = LoggerFactory.getLogger(GetProjectOfTaskService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public GetProjectOfTaskService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ResponseEntity<ProjectDTO> execute(Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		try {
			Project project = projectRepository.findByTaskId(taskId);
			if (project == null) {
				return ResponseEntity.notFound().build();
			}
			ProjectDTO dto = new ProjectDTO(project);
			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			logger.error("An exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
