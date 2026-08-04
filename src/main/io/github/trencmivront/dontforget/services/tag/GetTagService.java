package main.io.github.trencmivront.dontforget.services.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.TagDTO;
import main.io.github.trencmivront.dontforget.entities.Tag;
import main.io.github.trencmivront.dontforget.inter.Query;
import main.io.github.trencmivront.dontforget.repos.TagRepository;

@Service
public class GetTagService implements Query<Long, TagDTO>{

	private static final Logger logger = LoggerFactory.getLogger(GetTagService.class.getName());

	@Autowired
	private TagRepository tagRepository;

	public GetTagService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public ResponseEntity<TagDTO> execute(Long tagId) {
		logger.info("Executing {} for tagId: {}", this.getClass(), tagId);
		try {
			Tag tag = tagRepository.findById(tagId).orElse(null);
			if (tag == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(new TagDTO(tag));
		} catch (Exception e) {
			logger.warn("Error fetching tag with ID {}: {}", tagId, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
