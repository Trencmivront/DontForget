package main.java.services.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.TagDTO;
import main.java.entities.Tag;
import main.java.repos.TagRepository;

@Service
public class GetTagsService {

	private static final Logger logger = LoggerFactory.getLogger(GetTagsService.class.getName());

	@Autowired
	private TagRepository tagRepository;

	public GetTagsService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public ResponseEntity<List<TagDTO>> execute() {
		logger.info("Executing {}", this.getClass());
		try {
			List<Tag> tags = tagRepository.findAll();
			List<TagDTO> dtos = tags.stream()
				.map(tag -> new TagDTO(tag))
				.toList();
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			logger.error("Error fetching tags: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

}
