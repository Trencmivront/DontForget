package main.java.gui.panels;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.controllers.InboxController;
import main.java.custom.SpringContext;
import main.java.dto.InboxDTO;
import main.java.gui.panels.rows.MessageRowPanel;

public class InboxPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(InboxPanel.class.getName());

	private final InboxController inboxController;
	private final JScrollPane scrollPane = new JScrollPane();

	public InboxPanel() {
		logger.info("Initializing InboxPanel.");
		this.inboxController = SpringContext.getBean(InboxController.class);

		setLayout(new BorderLayout());
		add(new HeaderPanel("Messages"), BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		listMessages();

		logger.info("InboxPanel drawn.");
	}

	private void listMessages() {
		List<InboxDTO> inboxItems = inboxController.getInbox().getBody();

		if (inboxItems == null || inboxItems.isEmpty()) {
			scrollPane.setViewportView(new EmptyPanel("Your inbox is empty."));
		} else {
			JPanel rowsContainer = new JPanel();
			rowsContainer.setLayout(new BoxLayout(rowsContainer, BoxLayout.Y_AXIS));

			for (InboxDTO item : inboxItems) {
				rowsContainer.add(new MessageRowPanel(item));
			}

			scrollPane.setViewportView(rowsContainer);
		}
	}
}
