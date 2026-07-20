package main.java.services.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Tag;
import main.java.repos.TagRepository;

@Service
public class GetTagService {

	private static final Logger logger = LoggerFactory.getLogger(GetTagService.class.getName());

	@Autowired
	private TagRepository tagRepository;

	public GetTagService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public ResponseEntity<Tag> execute(Long tagId) {
		logger.info("Executing {} for tagId: {}", this.getClass(), tagId);
		try {
			Tag tag = tagRepository.findById(tagId).orElse(null);
			if (tag == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(tag);
		} catch (Exception e) {
			logger.warn("Error fetching tag with ID {}: {}", tagId, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
