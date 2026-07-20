package main.java.gui.panels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class EmptyPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(EmptyPanel.class.getName());
	
	private static final long serialVersionUID = 1L;

	public EmptyPanel(String message) {
		logger.info("Initializing EmptyPanel");
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(60, 20, 60, 20));

		JLabel emptyLabel = new JLabel(message, SwingConstants.CENTER);
		emptyLabel.setFont(new Font("Dialog", Font.ITALIC, 16));
		add(emptyLabel);
	}

}
