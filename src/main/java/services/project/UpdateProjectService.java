package main.java.services.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.ProjectDTO;
import main.java.entities.Project;
import main.java.inter.Command;
import main.java.repos.ProjectRepository;

@Service
public class UpdateProjectService implements Command<ProjectDTO>{

	private static final Logger logger = LoggerFactory.getLogger(UpdateProjectService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public UpdateProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ResponseEntity<String> execute(ProjectDTO p) {
		if (p == null) {
			logger.warn("ProjectDCO is null. Aborting update.");
			return ResponseEntity.badRequest().body("NULL PROJECT");
		}
		
		Long id = p.getProjectId();
		logger.info("Executing {} for p: {}, id: {}", this.getClass(), p, id);

		try {
			Project project = projectRepository.findById(id).orElse(null);
			if (project == null) {
				logger.warn("Project not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PROJECT NOT FOUND");
			}

			project.setprojectTitle(p.getProjectTitle());
			project.setDescription(p.getDescription() == null || p.getDescription().isEmpty() ? null : p.getDescription());
			project.seticonColorId(p.getIconColorId());

			projectRepository.save(project);
			logger.info("Project update complete.");
			return ResponseEntity.ok("PROJECT UPDATED");
		} catch (Exception e) {
			logger.error("Exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO UPDATE PROJECT");
		}
	}
}
