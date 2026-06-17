package app.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import app.cmp.CustomIcon;
import app.entities.IconColor;
import app.entities.Tag;
import app.services.GetIconColorOfTagService;
import app.services.GetTagsService;

public class TagPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TagPanel.class.getName());
	private JScrollPane scrollPane;

	public TagPanel() {
		logger.info("Drawing TagPanel.");
		setLayout(new BorderLayout());
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel tagActionsPanel = new JPanel();
		add(tagActionsPanel, BorderLayout.SOUTH);
		
		listTags();
	}
	
	private void listTags() {
		List<Tag> tags = null;
		scrollPane.removeAll();

		tags = GetTagsService.execute();
		
		if (tags == null || tags.isEmpty()) {
			scrollPane.setViewportView(new EmptyPanel("No tag found."));
			return;
		}
		
		JPanel tagContainer = new JPanel();
		tagContainer.setLayout(new BoxLayout(tagContainer, BoxLayout.Y_AXIS));
		
		for (Tag tag : tags) {
			JCheckBox ck = new JCheckBox();
			JLabel label = new JLabel(tag.tag_name());
			label.setFont(new Font("Dialog", Font.PLAIN, 20));
			
			JPanel tagRow = new JPanel();
			tagRow.setLayout(new BoxLayout(tagRow, BoxLayout.X_AXIS));
			tagRow.setAlignmentX(LEFT_ALIGNMENT);
			
			IconColor ic = GetIconColorOfTagService.execute(tag.tag_id());
			// if color not found, make it gray
			Color color = (ic == null) ? Color.GRAY:new Color(ic.red(), ic.green(), ic.blue());
			
			label.setIcon(new CustomIcon(color, 12, 12));
			
			tagRow.add(ck);
			tagRow.add(label);
			
			tagContainer.add(tagRow);
		}
		
		JViewport viewport = new JViewport();
		viewport.setFont(new Font("Dialog", Font.PLAIN, 20));
		viewport.add(tagContainer);
		
		scrollPane.setViewport(viewport);
	}

}

