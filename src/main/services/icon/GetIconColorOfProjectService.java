package main.services.icon;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.IconColor;
import main.repos.IconColorRepository;

@Service
public class GetIconColorOfProjectService {

	private static final Logger logger = Logger.getLogger(GetIconColorOfProjectService.class.getName());

	@Autowired
	private IconColorRepository iconColorRepository;

	public IconColor execute(Long id) {
		logger.info("The class " + logger.getName() + " is executed.");
		return iconColorRepository.findByProjectId(id);
	}

}
