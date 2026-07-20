package main.java.services.icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.IconColor;
import main.java.repos.IconColorRepository;

@Service
public class GetIconColorOfProjectService {

	private static final Logger logger = LoggerFactory.getLogger(GetIconColorOfProjectService.class.getName());

	@Autowired
	private IconColorRepository iconColorRepository;

	public GetIconColorOfProjectService(IconColorRepository iconColorRepository) {
		this.iconColorRepository = iconColorRepository;
	}

	public ResponseEntity<IconColor> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			IconColor color = iconColorRepository.findByProjectId(id);
			if (color == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(color);
		} catch (Exception e) {
			logger.error("An exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

}
