package app.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.icons.FlatInternalFrameCloseIcon;

import app.App;
import app.entities.Inbox;
import app.services.DeleteMessageByIdService;
import app.services.GetInboxService;

public class InboxPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InboxPanel.class.getName());
	private final Connection conn;
	private JPanel listContainer;

	public InboxPanel() {
		logger.info("Initializing InboxPanel.");
		this.conn = App.connection;
		
		setLayout(new GridLayout(0, 1));

		// List Container
		listContainer = new JPanel();
		listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
		List<Inbox> inboxItems = null;
		if (conn != null) {
			inboxItems = GetInboxService.execute(conn);
		}

		if (inboxItems == null || inboxItems.isEmpty()) {
			JPanel emptyPanel = new JPanel(new BorderLayout());
			emptyPanel.setBorder(new EmptyBorder(60, 20, 60, 20));

			JLabel emptyLabel = new JLabel("Your inbox is empty.", SwingConstants.CENTER);
			emptyLabel.setFont(new Font("Dialog", Font.ITALIC, 16));
			emptyPanel.add(emptyLabel);

			listContainer.add(emptyPanel);
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			for (Inbox item : inboxItems) {
				JPanel itemPanel = new JPanel();
				itemPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
				itemPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

				// Message label
				JLabel messageLabel = new JLabel("<html><body style='width: 150px;'>" + item.message() + "</body></html>");
				itemPanel.add(messageLabel);

				// Timestamp label
				String dateStr = item.created_at() != null ? item.created_at().format(formatter) : "";
				JLabel dateLabel = new JLabel(dateStr);
				dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
				itemPanel.add(dateLabel);
				
				JButton deleteMessageButton = new JButton(new FlatInternalFrameCloseIcon());
				deleteMessageButton.setHorizontalAlignment(SwingConstants.RIGHT);
				deleteMessageButton.setMaximumSize(new Dimension(15, 15));
				itemPanel.add(deleteMessageButton);
				deleteMessageButton.putClientProperty("inbox_id", item.inbox_id());
				addDeleteButtonActionListener(deleteMessageButton, itemPanel);
				
				listContainer.add(itemPanel);
			}
		}

		JScrollPane scrollPane = new JScrollPane(listContainer);
		scrollPane.setBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);

		logger.info("InboxPanel drawn.");
	}
	
	private void addDeleteButtonActionListener(JButton button, JPanel container) {
		button.addActionListener(_->{
			int id = (int)button.getClientProperty("inbox_id");
			DeleteMessageByIdService.execute(conn, id);
			container.removeAll();
			container.setVisible(false);
			listContainer.revalidate();
			listContainer.repaint();
		});
	}
	
}
