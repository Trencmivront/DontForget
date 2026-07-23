package main.java.gui.panels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import main.java.custom.SpringContext;
import main.java.controllers.IconColorController;
import main.java.controllers.TagController;
import org.springframework.http.ResponseEntity;

import main.java.custom.CustomIcon;
import main.java.entities.IconColor;
import main.java.entities.Tag;
import main.java.gui.Main;

public class TagRowPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(TagRowPanel.class.getName());

	private static final long serialVersionUID = 1L;

	private IconColorController iconColorController;
	private TagController tagController;

	public TagRowPanel(Tag tag) {
		logger.info("Initializing TagRowPanel");
		this(null, tag);
	}
	
	public TagRowPanel(JCheckBox ck, Tag tag) {
		this.iconColorController = SpringContext.getBean(IconColorController.class);
		this.tagController = SpringContext.getBean(TagController.class);
		putClientProperty("tagId", tag.getTagId());
		putClientProperty("tagName", tag.getTagName());
		putClientProperty("iconColorId", tag.getIconColorId());

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);

		JLabel label = new JLabel(tag.getTagName());
		label.setFont(new Font("Dialog", Font.PLAIN, 20));

		IconColor ic = null;
		try {
			ResponseEntity<IconColor> response = iconColorController.getIconColorOfTag(tag.getTagId());
			ic = response.getBody();
		} catch (Exception e) {
			logger.error("Failed to fetch icon color for tag " + tag.getTagId(), e);
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
				Long tagId = (Long) getClientProperty("tagId");
				int code = 500;
				try {
					ResponseEntity<String> response = tagController.deleteTag(tagId);
					code = response.getStatusCode().value();
				} catch (Exception e) {
					logger.error("Failed to delete tag " + tagId, e);
				}
				if (code < 400) {
					refreshTagsList();
				} else {
					JOptionPane.showMessageDialog(this, "Failed to delete the tag.");
				}
		});
	}
	
	private void refreshTagsList() {
		Main.getMain().getTagsButton().doClick();
	}
}
