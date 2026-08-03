package main.java.gui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import main.java.controllers.TagController;
import main.java.custom.SpringContext;
import main.java.dto.TagDTO;
import main.java.gui.panels.rows.TagRowPanel;
import main.java.gui.windows.TagWindow;

public class TagsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TagsPanel.class.getName());
	private JScrollPane scrollPane;
	
	private final TagController tagController = SpringContext.getBean(TagController.class);
	private List<TagDTO> selectedTags = new ArrayList<TagDTO>();
	private static TagsPanel tagsPanel;
	
	public List<TagDTO> getSelectedTags(){
		logger.warn("Tags Panel: {}", selectedTags);
		return selectedTags;
	}
	
	public void setSelectedTags(List<TagDTO> selectedTags) {
		this.selectedTags = selectedTags;
	}
	
	public static TagsPanel getTagsPanel() {
		return tagsPanel;
	}
	
	public TagsPanel() {
//		I want a new instance every time I use it
		if(tagsPanel != null) {
			tagsPanel = null;
		}

		tagsPanel = this;
		
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
		ResponseEntity<List<TagDTO>> tagResponseEntity = tagController.getTags();
		List<TagDTO> tags = tagResponseEntity.getBody();
		
		JPanel tagContainer = new JPanel();
		tagContainer.setLayout(new BoxLayout(tagContainer, BoxLayout.Y_AXIS));

		// "Add Tag" button at the top of the list
		JButton addTagButton = new JButton("+ Add Tag");
		addTagButton.addActionListener(_ -> {
			TagWindow tagWindow = new TagWindow();
			tagWindow.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					listTags();
				}
			});
		});
		tagContainer.add(addTagButton);

		if (tags == null || tags.isEmpty()) {
			scrollPane.setViewportView(tagContainer);
			revalidate();
			repaint();
			return;
		}
		
		for (TagDTO tag : tags) {
			JCheckBox ck = new JCheckBox();
			if(selectedTags.contains(tag)) {
				ck.setSelected(true);
			}
			ck.addActionListener(_->{
				if(ck.isSelected()) {
					selectedTags.add(tag);
				} else {
					selectedTags.remove(tag);
				}
			});
			JPanel tagRow = new TagRowPanel(ck, tag);
			tagContainer.add(tagRow);
		}
		
		JViewport viewport = new JViewport();
		viewport.setFont(new Font("Dialog", Font.PLAIN, 20));
		viewport.setView(tagContainer);
		
		scrollPane.setViewport(viewport);
		revalidate();
		repaint();
	}

}
