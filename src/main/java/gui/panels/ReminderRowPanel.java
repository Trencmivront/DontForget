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

import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.api.Api;
import main.java.entities.Reminder;
import main.java.entities.Task;
import main.java.gui.Main;
import main.java.gui.windows.CreateUpdateTaskWindow;

public class ReminderRowPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(ReminderRowPanel.class.getName());

	private static final long serialVersionUID = 1L;
	private final Api api = new Api();
	private final ObjectMapper mapper = new ObjectMapper();

	public ReminderRowPanel(Reminder reminder, Task task) {
		logger.info("Initializing ReminderRowPanel");
		setLayout(new BorderLayout());
		
		putClientProperty("taskId", task.taskId());
		putClientProperty("taskTitle", task.taskTitle());
		putClientProperty("description", task.description());
		putClientProperty("statusId", task.statusId());
		putClientProperty("priority", task.priority());
		putClientProperty("dueDate", task.dueDate());
		putClientProperty("listOrder", task.listOrder());
		putClientProperty("projectId", task.projectId());
		putClientProperty("createdAt", task.createdAt());
		putClientProperty("updatedAt", task.updatedAt());
		putClientProperty("completedAt", task.completedAt());
		
		putClientProperty("remindAt", reminder.remindAt());
		putClientProperty("message", reminder.message());

		JLabel title = new JLabel(task.taskTitle());
		title.setHorizontalTextPosition(SwingConstants.CENTER);
		add(title, BorderLayout.CENTER);

		LocalDateTime localDateTime = reminder.remindAt().toLocalDateTime();
		setToolTipText(localDateTime.getDayOfMonth() + " " + localDateTime.getMonth().name() + " " + localDateTime.getYear());

		addMouseListeners();
	}

	private void addMouseListeners() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		popupMenu.add(deleteItem);
		
		deleteItem.addActionListener(_ -> {
			Long taskId = (Long) getClientProperty("taskId");
			int code = api.delete("/api/reminder/delete/", taskId);
			if (code < 400) {
				Main.getMain().getRemindersButton().doClick();
			} else {
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
						String res = api.get("/api/task/get/", taskId);
						task = mapper.readValue(res, Task.class);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					if (task != null) {
						new CreateUpdateTaskWindow(Main.getMain(), task.projectId(), true, ReminderRowPanel.this);
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) { // Right click
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
}
