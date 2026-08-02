package main.java.services.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.ProjectDTO;
import main.java.entities.Project;
import main.java.inter.Query;
import main.java.repos.ProjectRepository;

@Service
public class GetProjectByIdService implements Query<Long, ProjectDTO> {

	private static final Logger logger = LoggerFactory.getLogger(GetProjectByIdService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public GetProjectByIdService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ResponseEntity<ProjectDTO> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			Project project = projectRepository.findById(id).orElse(null);
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
