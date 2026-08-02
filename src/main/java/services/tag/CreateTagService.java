package main.java.services.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.dto.TagDTO;
import main.java.entities.Tag;
import main.java.inter.Post;
import main.java.repos.TagRepository;

@Service
public class CreateTagService implements Post<TagDTO>{

	@Autowired
	private TagRepository tagRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(CreateTagService.class);
	
	public CreateTagService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	@Override
	public ResponseEntity<Long> execute(TagDTO tagDTO) {
		logger.info("Executing {} for tag: {}", this.getClass(), tagDTO);
		
		try {
			Tag tag = new Tag();
			tag.setTagName(tagDTO.getTagName());
			tag.setIconColorId(tagDTO.getIconColorId());
			
			Long id = tagRepository.save(tag).getTagId();
			
			return ResponseEntity.ok(id);
		}catch (Exception _) {
			logger.warn("Error while creating tag: {}", tagDTO);
			return ResponseEntity.ofNullable(0l);
		}
	}
}
