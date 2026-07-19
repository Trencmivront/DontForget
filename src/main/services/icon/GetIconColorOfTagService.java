package main.services.icon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.IconColor;
import main.repos.IconColorRepository;

@Service
public class GetIconColorOfTagService {

	@Autowired
	private IconColorRepository iconColorRepository;

	public IconColor execute(Long id) {
		return iconColorRepository.findByTagId(id);
	}

}
