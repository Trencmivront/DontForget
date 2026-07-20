package main.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.dco.ProjectDCO;
import main.java.entities.Project;
import main.java.services.project.CreateProjectService;
import main.java.services.project.DeleteProjectService;
import main.java.services.project.GetProjectOfTaskService;
import main.java.services.project.GetProjectsService;
import main.java.services.project.UpdateProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class.getName());

	private final CreateProjectService createProjectService;
	private final DeleteProjectService deleteProjectService;
	private final GetProjectOfTaskService getProjectOfTaskService;
	private final GetProjectsService getProjectsService;
	private final UpdateProjectService updateProjectService;

	public ProjectController(CreateProjectService createProjectService,
			DeleteProjectService deleteProjectService,
			GetProjectOfTaskService getProjectOfTaskService,
			GetProjectsService getProjectsService,
			UpdateProjectService updateProjectService) {
		logger.info("Initializing ProjectController");
		this.createProjectService = createProjectService;
		this.deleteProjectService = deleteProjectService;
		this.getProjectOfTaskService = getProjectOfTaskService;
		this.getProjectsService = getProjectsService;
		this.updateProjectService = updateProjectService;
	}

	@PostMapping("/create")
	public ResponseEntity<Long> createProject(@RequestBody ProjectDCO p) {
		logger.info("Executing {} for project: {}", this.getClass(), p);
		return createProjectService.execute(p);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteProject(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteProjectService.execute(id);
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<Project> getProjectOfTask(@PathVariable Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		return getProjectOfTaskService.execute(taskId);
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<Project>> getProjects() {
		logger.info("Executing {}", this.getClass());
		return getProjectsService.execute();
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateProject(@RequestBody ProjectDCO p, @PathVariable Long id) {
		logger.info("Executing {} for p: {}, id: {}", this.getClass(), p, id);
		return updateProjectService.execute(p, id);
	}
}
