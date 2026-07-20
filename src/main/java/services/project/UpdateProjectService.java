package main.java.services.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dco.ProjectDCO;
import main.java.entities.Project;
import main.java.repos.ProjectRepository;

@Service
public class UpdateProjectService {

	private static final Logger logger = LoggerFactory.getLogger(UpdateProjectService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public UpdateProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ResponseEntity<String> execute(ProjectDCO p, Long id) {
		logger.info("Executing {} for p: {}, id: {}", this.getClass(), p, id);

		if (p == null) {
			logger.warn("ProjectDCO is null. Aborting update.");
			return ResponseEntity.badRequest().body("NULL PROJECT");
		}

		try {
			Project project = projectRepository.findById(id).orElse(null);
			if (project == null) {
				logger.warn("Project not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PROJECT NOT FOUND");
			}

			project.setprojectTitle(p.projectTitle());
			project.setDescription(p.description() == null || p.description().isEmpty() ? null : p.description());
			project.seticonColorId(p.iconColorId());

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
