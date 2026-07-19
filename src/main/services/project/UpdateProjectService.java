package main.services.project;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dco.ProjectDCO;
import main.entities.Project;
import main.repos.ProjectRepository;

@Service
public class UpdateProjectService {

	private static final Logger logger = Logger.getLogger(UpdateProjectService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public boolean execute(ProjectDCO p, Long id) {
		logger.info("Class " + logger.getName() + " is executed with project ID: " + id);

		if (p == null) {
			logger.warning("ProjectDCO is null. Aborting update.");
			return false;
		}

		try {
			Project project = projectRepository.findById(id).orElse(null);
			if (project == null) {
				logger.warning("Project not found with ID: " + id);
				return false;
			}

			project.setProject_title(p.project_title());
			project.setDescription(p.description() == null || p.description().isEmpty() ? null : p.description());
			project.setIcon_color_id(p.icon_color_id());

			projectRepository.save(project);
			logger.info("Project update complete.");
			return true;
		} catch (Exception e) {
			logger.severe("Exception occurred: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
