package main.gui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import main.entities.Tag;
import main.services.tag.GetTagsService;

public class TagsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TagsPanel.class.getName());
	private JScrollPane scrollPane;

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
		List<Tag> tags = null;
		scrollPane.removeAll();

		tags = new GetTagsService().execute();
		
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

