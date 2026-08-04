package main.java.gui.panels.rows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.controllers.InboxController;
import main.java.custom.SpringContext;
import main.java.dto.InboxDTO;
import main.java.gui.Main;

public class MessageRowPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(MessageRowPanel.class.getName());

	private static final Color BG_DEFAULT  = Color.lightGray;
	private static final Color BG_HOVER    = Color.GRAY;

	private final InboxController inboxController = SpringContext.getBean(InboxController.class);
	private final InboxDTO inboxDTO;

	public MessageRowPanel(InboxDTO inboxDTO) {
		logger.info("Initializing MessageRowPanel for inbox id: {}", inboxDTO.getInboxId());
		this.inboxDTO = inboxDTO;

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(BG_DEFAULT);
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		setAlignmentX(LEFT_ALIGNMENT);
		setBorder(new MatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

		// --- Message label (fills remaining space) ---
		JLabel messageLabel = new JLabel(inboxDTO.getMessage());
		messageLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
		messageLabel.setBorder(new EmptyBorder(0, 8, 0, 8));

		// --- Date label (fixed width) ---
		String dateText = inboxDTO.getCreatedAt() != null
				? inboxDTO.getCreatedAt().toLocalDateTime()
						.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
				: "";
		JLabel dateLabel = new JLabel(dateText);
		dateLabel.setFont(new Font("Dialog", Font.ITALIC, 11));
		dateLabel.setForeground(new Color(120, 120, 120));
		dateLabel.setBorder(new EmptyBorder(0, 4, 0, 8));
		dateLabel.setPreferredSize(new Dimension(160, 40));
		dateLabel.setMinimumSize(new Dimension(160, 40));
		dateLabel.setMaximumSize(new Dimension(160, 40));

		// --- Delete button ---
		JButton deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		deleteButton.setFocusPainted(false);
		deleteButton.setPreferredSize(new Dimension(80, 26));
		deleteButton.setMaximumSize(new Dimension(80, 26));
		deleteButton.addActionListener(_ -> handleDelete());

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);

		add(messageLabel);
		add(separator);
		
		add(dateLabel);
		add(separator);
		
		add(deleteButton);

		addHoverEffect();
	}

	private void addHoverEffect() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(BG_HOVER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(BG_DEFAULT);
			}
		});
	}

	private void handleDelete() {
		try {
			inboxController.deleteMessageById(inboxDTO.getInboxId());
			// Refresh the inbox panel by re-clicking the inbox nav button
			Main.getMain().getInboxButton().doClick();
		} catch (Exception e) {
			logger.error("Failed to delete inbox item id: {}", inboxDTO.getInboxId(), e);
			JOptionPane.showMessageDialog(this, "Failed to delete the message.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
