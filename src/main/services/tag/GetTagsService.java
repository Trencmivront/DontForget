package main.services.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Tag;
import main.repos.TagRepository;

@Service
public class GetTagsService {

	@Autowired
	private TagRepository tagRepository;

	public List<Tag> execute() {
		List<Tag> tags = tagRepository.findAll();
		return tags.isEmpty() ? null : tags;
	}

}
