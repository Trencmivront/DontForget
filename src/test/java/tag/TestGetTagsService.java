package test.java.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.java.entities.Tag;
import main.java.repos.TagRepository;
import main.java.services.tag.GetTagsService;

@ExtendWith(MockitoExtension.class)
class TestGetTagsService {

	@Mock
	private TagRepository tagRepository;
	
	@InjectMocks
	private GetTagsService getTagsService;
	
	private Tag sampleTag;
	
	@BeforeEach
	void init() {
		sampleTag = new Tag();
		sampleTag.setTagId(1L);
		sampleTag.setTagName("Urgent");
		sampleTag.setIconColorId(2L);
	}
	
	@Test
	void testServiceReturnsTrueValue() {
		// Mock behavior
		when(tagRepository.findAll()).thenReturn(List.of(sampleTag));
		
		// Call service method
		ResponseEntity<List<Tag>> response = getTagsService.execute();
		List<Tag> tags = response.getBody();
		
		// Assertions
		assertNotNull(tags);
		assertEquals(1, tags.size());
		assertEquals("Urgent", tags.get(0).getTagName());
		assertEquals(Long.valueOf(2L), tags.get(0).getIconColorId());
		
		// Verify interactions
		verify(tagRepository).findAll();
	}
	
	@Test
	void testServiceReturnsNullValueOnException() {
		// Mock exception
		when(tagRepository.findAll()).thenThrow(new RuntimeException("DB error"));
		
		// Call service method
		ResponseEntity<List<Tag>> response = getTagsService.execute();
		
		// Assertions
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		// Verify interactions
		verify(tagRepository).findAll();
	}
}
