package main.services.project;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Project;
import main.repos.ProjectRepository;

@Service
public class GetProjectsService {

	private static final Logger logger = Logger.getLogger(GetProjectsService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public List<Project> execute() {
		logger.info("Class " + logger.getName() + " is executed.");
		try {
			return projectRepository.findAllByOrderByListOrderAsc();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
