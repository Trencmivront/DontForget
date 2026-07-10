package main.gui.panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.cmp.CustomIcon;
import main.entities.IconColor;
import main.entities.Tag;
import main.services.icon.GetIconColorOfTagService;

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

		IconColor ic = GetIconColorOfTagService.execute(tag.tag_id());
		// if color not found, make it gray
		Color color = (ic == null) ? Color.GRAY : new Color(ic.red(), ic.green(), ic.blue());

		label.setIcon(new CustomIcon(color, 12, 12));

		if (ck != null) {
			add(ck);
		}
		add(label);
	}
}
