package main.java.gui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.custom.SpringContext;
import org.springframework.http.ResponseEntity;
import main.java.controllers.TagController;
import main.java.entities.Tag;

public class TagsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TagsPanel.class.getName());
	private JScrollPane scrollPane;
	
	private final TagController tagController = SpringContext.getBean(TagController.class);
	
	public TagsPanel() {
		
		logger.info("Drawing TagsPanel.");
		
		setLayout(new BorderLayout());
		
		add(new HeaderPanel("Tags"), BorderLayout.NORTH);
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel tagActionsPanel = new JPanel();
		add(tagActionsPanel, BorderLayout.SOUTH);
		
		listTags();
	}
	
	private void listTags() {
		ResponseEntity<List<Tag>> tagResponseEntity = tagController.getTags();
		List<Tag> tags = tagResponseEntity.getBody();
		
		if (tags == null || tags.isEmpty()) {
			scrollPane.setViewportView(new EmptyPanel("No tag found."));
			return;
		}
		
		JPanel tagContainer = new JPanel();
		tagContainer.setLayout(new BoxLayout(tagContainer, BoxLayout.Y_AXIS));
		
		for (Tag tag : tags) {
			JCheckBox ck = new JCheckBox();
			JPanel tagRow = new TagRowPanel(ck, tag);
			tagContainer.add(tagRow);
		}
		
		JViewport viewport = new JViewport();
		viewport.setFont(new Font("Dialog", Font.PLAIN, 20));
		viewport.add(tagContainer);
		
		scrollPane.setViewport(viewport);
	}

}
