package app.gui.panels;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class EmptyPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public EmptyPanel(String message) {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(60, 20, 60, 20));

		JLabel emptyLabel = new JLabel(message, SwingConstants.CENTER);
		emptyLabel.setFont(new Font("Dialog", Font.ITALIC, 16));
		add(emptyLabel);
	}

}
