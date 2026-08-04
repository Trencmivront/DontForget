package main.io.github.trencmivront.dontforget.gui.panels.rows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.springframework.http.ResponseEntity;

import main.io.github.trencmivront.dontforget.controllers.IconColorController;
import main.io.github.trencmivront.dontforget.controllers.TagController;
import main.io.github.trencmivront.dontforget.custom.CustomIcon;
import main.io.github.trencmivront.dontforget.custom.SpringContext;
import main.io.github.trencmivront.dontforget.dto.IconColorDTO;
import main.io.github.trencmivront.dontforget.dto.TagDTO;
import main.io.github.trencmivront.dontforget.gui.Main;
import main.io.github.trencmivront.dontforget.gui.panels.TagsPanel;

public class TagRowPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(TagRowPanel.class.getName());

	private static final long serialVersionUID = 1L;

	private IconColorController iconColorController;
	private TagController tagController;
	private TagDTO tagDTO;

	public TagRowPanel(TagDTO tagDTO) {
		logger.info("Initializing TagRowPanel");
		this(null, tagDTO);
	}
	
	public TagRowPanel(JCheckBox ck, TagDTO tagDTO) {
		this.iconColorController = SpringContext.getBean(IconColorController.class);
		this.tagController = SpringContext.getBean(TagController.class);
		
		this.tagDTO = tagDTO;

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);

		JLabel label = new JLabel(tagDTO.getTagName());
		label.setFont(new Font("Dialog", Font.PLAIN, 20));

		IconColorDTO ic = null;
		try {
			ResponseEntity<IconColorDTO> response = iconColorController.getIconColorOfTag(tagDTO.getTagId());
			ic = response.getBody();
		} catch (Exception e) {
			logger.error("Failed to fetch icon color for tag " + tagDTO.getTagId(), e);
		}
		// if color not found, make it gray
		Color color = (ic == null) ? Color.GRAY : new Color(ic.getRed(), ic.getGreen(), ic.getBlue());

		label.setIcon(new CustomIcon(color, 12, 12));

		if (ck != null) {
			add(ck);
		}
		add(label);
		addPopUpMenuItem();
	}

	private void addPopUpMenuItem() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		popupMenu.add(deleteItem);
		
		addDeleteActionListener(deleteItem);
		addPopupMenuMouseListener(popupMenu);
	}
	
	private void addPopupMenuMouseListener(JPopupMenu popupMenu) {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					showPopup(e);
				}
			}
			private void showPopup(MouseEvent e) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	private void addDeleteActionListener(JMenuItem deleteItem) {
		deleteItem.addActionListener(_ -> {
				Long tagId = tagDTO.getTagId();
				
				List<TagDTO> selectedTags = TagsPanel.getTagsPanel().getSelectedTags();
				
				if(!selectedTags.isEmpty() && selectedTags != null) {
					int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete tags?", "Delete Selected Tags",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if(option == JOptionPane.YES_OPTION) {
						for(TagDTO tag : selectedTags) {
							tagController.deleteTag(tag.getTagId());
						}
						refreshTagsList();
						return;
					}
					else return;
					
				}
				
				tagController.deleteTag(tagId);
				refreshTagsList();
		});
	}
	
	private void refreshTagsList() {
		Main.getMain().getTagsButton().doClick();
	}
}
