package main.io.github.trencmivront.dontforget.services.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.TagDTO;
import main.io.github.trencmivront.dontforget.entities.Tag;
import main.io.github.trencmivront.dontforget.inter.Post;
import main.io.github.trencmivront.dontforget.repos.TagRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(0l);
		}
	}
}
