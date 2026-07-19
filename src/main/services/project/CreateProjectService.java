package main.services.project;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dco.ProjectDCO;
import main.entities.Project;
import main.repos.ProjectRepository;

@Service
public class CreateProjectService {

	private static final Logger logger = Logger.getLogger(CreateProjectService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public boolean execute(ProjectDCO p) {
		logger.info("Class " + logger.getName() + " is executed.");
		try {
			// Compute list_order as MAX + 1
			int listOrder = projectRepository.findMaxListOrder() + 1;

			Project project = new Project();
			project.setProject_title(p.project_title());
			project.setDescription(p.description());
			project.setList_order(listOrder);
			project.setIcon_color_id(p.icon_color_id());

			projectRepository.save(project);
			return true;
		} catch (Exception e) {
			logger.warning(e.getClass().getName() + " thrown.");
			e.printStackTrace();
			return false;
		}
	}

}
