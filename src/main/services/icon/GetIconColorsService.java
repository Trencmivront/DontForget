package main.services.icon;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.IconColor;
import main.repos.IconColorRepository;

@Service
public class GetIconColorsService {

	private static final Logger logger = Logger.getLogger(GetIconColorsService.class.getName());

	@Autowired
	private IconColorRepository iconColorRepository;

	public List<IconColor> execute() {
		try {
			return iconColorRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
