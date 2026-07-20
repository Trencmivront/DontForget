package main.java.services.icon;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.IconColor;
import main.java.repos.IconColorRepository;

@Service
public class GetIconColorsService {

	private static final Logger logger = LoggerFactory.getLogger(GetIconColorsService.class.getName());

	@Autowired
	private IconColorRepository iconColorRepository;

	public GetIconColorsService(IconColorRepository iconColorRepository) {
		this.iconColorRepository = iconColorRepository;
	}

	public ResponseEntity<List<IconColor>> execute() {
		logger.info("Executing {}", this.getClass());
		try {
			logger.info("Class {} executed", this.getClass());
			List<IconColor> colors = iconColorRepository.findAll();
			return ResponseEntity.ok(colors);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Collections.emptyList());
		}
	}

}
