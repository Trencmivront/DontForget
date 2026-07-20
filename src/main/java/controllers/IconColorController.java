package main.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.entities.IconColor;
import main.java.services.icon.GetIconColorOfProjectService;
import main.java.services.icon.GetIconColorOfTagService;
import main.java.services.icon.GetIconColorsService;

@RestController
@RequestMapping("/api/icon-color")
public class IconColorController {

	private static final Logger logger = LoggerFactory.getLogger(IconColorController.class.getName());

	private final GetIconColorOfProjectService getIconColorOfProjectService;
	private final GetIconColorOfTagService getIconColorOfTagService;
	private final GetIconColorsService getIconColorsService;

	public IconColorController(GetIconColorOfProjectService getIconColorOfProjectService,
			GetIconColorOfTagService getIconColorOfTagService,
			GetIconColorsService getIconColorsService) {
		logger.info("Initializing IconColorController");
		this.getIconColorOfProjectService = getIconColorOfProjectService;
		this.getIconColorOfTagService = getIconColorOfTagService;
		this.getIconColorsService = getIconColorsService;
	}

	@GetMapping("/project/{id}")
	public ResponseEntity<IconColor> getIconColorOfProject(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return getIconColorOfProjectService.execute(id);
	}

	@GetMapping("/tag/{id}")
	public ResponseEntity<IconColor> getIconColorOfTag(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return getIconColorOfTagService.execute(id);
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<IconColor>> getIconColors() {
		logger.info("Executing {}", this.getClass());
		return getIconColorsService.execute();
	}
}
