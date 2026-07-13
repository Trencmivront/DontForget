package main.gui.panels;

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

import main.cmp.CustomIcon;
import main.entities.IconColor;
import main.entities.Tag;
import main.gui.Main;
import main.services.icon.GetIconColorOfTagService;
import main.services.tag.DeleteTagService;

public class TagRowPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TagRowPanel(Tag tag) {
		this(null, tag);
	}
	
	public TagRowPanel(JCheckBox ck, Tag tag) {
		putClientProperty("tag_id", tag.tag_id());
		putClientProperty("tag_name", tag.tag_name());
		putClientProperty("icon_color_id", tag.icon_color_id());

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);

		JLabel label = new JLabel(tag.tag_name());
		label.setFont(new Font("Dialog", Font.PLAIN, 20));

		IconColor ic = new GetIconColorOfTagService().execute(tag.tag_id());
		// if color not found, make it gray
		Color color = (ic == null) ? Color.GRAY : new Color(ic.red(), ic.green(), ic.blue());

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
				Long tagId = (Long) getClientProperty("tag_id");
				if (new DeleteTagService().execute(tagId)) {
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
