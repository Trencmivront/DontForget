package main.io.github.trencmivront.dontforget.services.project;

import java.util.List;
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
public class GetProjectsService implements Query<Void, List<ProjectDTO>>{

	private static final Logger logger = LoggerFactory.getLogger(GetProjectsService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public GetProjectsService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ResponseEntity<List<ProjectDTO>> execute(Void i) {
		logger.info("Executing {}", this.getClass());
		try {
			List<Project> projects = projectRepository.findAllByOrderByListOrderAsc();
			List<ProjectDTO> dtos = projects.stream()
				.map(ProjectDTO::new)
				.toList();
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			logger.error("An exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
