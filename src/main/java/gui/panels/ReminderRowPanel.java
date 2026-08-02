package main.java.gui.panels;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import main.java.controllers.ReminderController;
import main.java.custom.SpringContext;
import main.java.dto.ReminderDTO;
import main.java.dto.TaskDTO;
import main.java.gui.Main;
import main.java.gui.popups.ReminderDialog;

public class ReminderRowPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ReminderRowPanel.class.getName());

	private ReminderController reminderController;
	private TaskDTO taskDTO;
	
	public ReminderRowPanel(ReminderDTO reminder, TaskDTO taskDTO) {
		logger.info("Initializing ReminderRowPanel");
		this.reminderController = SpringContext.getBean(ReminderController.class);
		this.taskDTO = taskDTO;
		setLayout(new BorderLayout());

		JLabel title = new JLabel(taskDTO.getTaskTitle());
		title.setHorizontalTextPosition(SwingConstants.CENTER);
		add(title, BorderLayout.CENTER);

		LocalDateTime localDateTime = reminder.getRemindAt();
		if (localDateTime != null) {
			setToolTipText(localDateTime.getDayOfMonth() + " " + localDateTime.getMonth().name() + " " + localDateTime.getYear());
		}

		addMouseListeners();
	}

	private void addMouseListeners() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		
		popupMenu.add(deleteItem);
		
		deleteItem.addActionListener(_ -> {
			Long taskId = taskDTO.getTaskId();
			try {
				ResponseEntity<String> response = reminderController.deleteReminder(taskId);
				if (response.getStatusCode().is2xxSuccessful()) {
					Main.getMain().getRemindersButton().doClick();
				} else {
					JOptionPane.showMessageDialog(this, "Failed to delete the reminder.");
				}
			} catch (Exception e) {
				logger.error("Failed to delete reminder", e);
				JOptionPane.showMessageDialog(this, "Failed to delete the reminder.");
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) { // Left click
					new ReminderDialog(taskDTO.getTaskId());
				} else if (e.getButton() == MouseEvent.BUTTON3) { // Right click
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
}
