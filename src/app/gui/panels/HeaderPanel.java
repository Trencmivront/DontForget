package app.gui.panels;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

public class HeaderPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public HeaderPanel(String title, String description) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel(title);
		JLabel descpLabel = new JLabel(description);
		
		titleLabel.setFont(new Font("Roboto",1, 30));
		descpLabel.setFont(new Font("Roboto", 0, 15));
		
		JSeparator separator = new JSeparator();
		
		setBorder(new EmptyBorder(3, 3, 5, 0));
		
		add(titleLabel);
		add(separator);
		add(descpLabel);
	}
	
	public HeaderPanel(String title) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Roboto",1, 30));
		
		JSeparator separator = new JSeparator();

		add(titleLabel);
		add(separator);
	}

}
