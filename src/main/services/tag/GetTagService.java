package main.services.tag;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Tag;
import main.repos.TagRepository;

@Service
public class GetTagService {

	private static final Logger logger = Logger.getLogger(GetTagService.class.getName());

	@Autowired
	private TagRepository tagRepository;

	public Tag execute(Long tagId) {
		logger.info("Executing GetTagService with tagId: " + tagId);
		try {
			return tagRepository.findById(tagId).orElse(null);
		} catch (Exception e) {
			logger.warning("Error fetching tag with ID " + tagId + ": " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
