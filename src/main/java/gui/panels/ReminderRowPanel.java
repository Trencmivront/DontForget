package main.java.gui.panels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import main.java.custom.SpringContext;
import main.java.controllers.ReminderController;
import main.java.controllers.TaskController;
import org.springframework.http.ResponseEntity;

import main.java.entities.Reminder;
import main.java.entities.Task;
import main.java.gui.Main;
import main.java.gui.windows.CreateUpdateTaskWindow;

public class ReminderRowPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ReminderRowPanel.class.getName());

	private ReminderController reminderController;
	private TaskController taskController;

	public ReminderRowPanel(Reminder reminder, Task task) {
		logger.info("Initializing ReminderRowPanel");
		this.reminderController = SpringContext.getBean(ReminderController.class);
		this.taskController = SpringContext.getBean(TaskController.class);
		setLayout(new BorderLayout());
		
		putClientProperty("taskId", task.getTaskId());
		putClientProperty("taskTitle", task.getTaskTitle());
		putClientProperty("description", task.getDescription());
		putClientProperty("statusId", task.getStatusId());
		putClientProperty("priority", task.getPriority());
		putClientProperty("dueDate", task.getDueDate());
		putClientProperty("listOrder", task.getListOrder());
		putClientProperty("projectId", task.getProjectId());
		putClientProperty("createdAt", task.getCreatedAt());
		putClientProperty("updatedAt", task.getUpdatedAt());
		putClientProperty("completedAt", task.getCompletedAt());
		
		putClientProperty("remindAt", reminder.getRemindAt());
		putClientProperty("message", reminder.getMessage());

		JLabel title = new JLabel(task.getTaskTitle());
		title.setHorizontalTextPosition(SwingConstants.CENTER);
		add(title, BorderLayout.CENTER);

		LocalDateTime localDateTime = reminder.getRemindAt().toLocalDateTime();
		setToolTipText(localDateTime.getDayOfMonth() + " " + localDateTime.getMonth().name() + " " + localDateTime.getYear());

		addMouseListeners();
	}

	private void addMouseListeners() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		popupMenu.add(deleteItem);
		
		deleteItem.addActionListener(_ -> {
			Long taskId = (Long) getClientProperty("taskId");
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
					Long taskId = (Long) getClientProperty("taskId");
					Task task = null;
					try {
						ResponseEntity<Task> response = taskController.getTaskById(taskId);
						task = response.getBody();
					} catch (Exception ex) {
						logger.error("Failed to get task", ex);
					}
					if (task != null) {
						new CreateUpdateTaskWindow(Main.getMain(), task.getProjectId(), true, ReminderRowPanel.this);
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) { // Right click
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
}
