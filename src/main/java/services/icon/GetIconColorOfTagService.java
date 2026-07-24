package main.java.services.icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.IconColorDTO;
import main.java.entities.IconColor;
import main.java.repos.IconColorRepository;

@Service
public class GetIconColorOfTagService {

	private static final Logger logger = LoggerFactory.getLogger(GetIconColorOfTagService.class.getName());

	@Autowired
	private IconColorRepository iconColorRepository;

	public GetIconColorOfTagService(IconColorRepository iconColorRepository) {
		this.iconColorRepository = iconColorRepository;
	}

	public ResponseEntity<IconColorDTO> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			IconColor color = iconColorRepository.findByTagId(id);
			if (color == null) {
				return ResponseEntity.notFound().build();
			}
			IconColorDTO dto = new IconColorDTO(color.getIconColorId(), color.getRed(), color.getGreen(), color.getBlue());
			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			logger.error("An exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

}
