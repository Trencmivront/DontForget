package main.io.github.trencmivront.dontforget.services.icon;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.IconColorDTO;
import main.io.github.trencmivront.dontforget.entities.IconColor;
import main.io.github.trencmivront.dontforget.repos.IconColorRepository;

@Service
public class GetIconColorsService {

	private static final Logger logger = LoggerFactory.getLogger(GetIconColorsService.class.getName());

	@Autowired
	private IconColorRepository iconColorRepository;

	public GetIconColorsService(IconColorRepository iconColorRepository) {
		this.iconColorRepository = iconColorRepository;
	}

	public ResponseEntity<List<IconColorDTO>> execute() {
		logger.info("Executing {}", this.getClass());
		try {
			logger.info("Class {} executed", this.getClass());
			List<IconColor> colors = iconColorRepository.findAll();
			List<IconColorDTO> dtos = colors.stream()
				.map(IconColorDTO::new).toList();
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Collections.emptyList());
		}
	}

}
