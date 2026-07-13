package main.gui.panels;

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

import main.entities.Reminder;
import main.entities.Task;
import main.gui.Main;
import main.gui.windows.CreateUpdateTaskWindow;
import main.services.reminder.DeleteReminderService;
import main.services.task.GetTaskByIdService;

public class ReminderRowPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ReminderRowPanel(Reminder reminder, Task task) {
		setLayout(new BorderLayout());
		
		putClientProperty("task_id", task.task_id());
		putClientProperty("task_title", task.task_title());
		putClientProperty("description", task.description());
		putClientProperty("status_id", task.status_id());
		putClientProperty("priority", task.priority());
		putClientProperty("due_date", task.due_date());
		putClientProperty("list_order", task.list_order());
		putClientProperty("project_id", task.project_id());
		putClientProperty("created_at", task.created_at());
		putClientProperty("updated_at", task.updated_at());
		putClientProperty("completed_at", task.completed_at());
		
		putClientProperty("remind_at", reminder.remind_at());
		putClientProperty("cstm_message", reminder.cstm_message());

		JLabel title = new JLabel(task.task_title());
		title.setHorizontalTextPosition(SwingConstants.CENTER);
		add(title, BorderLayout.CENTER);

		LocalDateTime localDateTime = reminder.remind_at().toLocalDateTime();
		setToolTipText(localDateTime.getDayOfMonth() + " " + localDateTime.getMonth().name() + " " + localDateTime.getYear());

		addMouseListeners();
	}

	private void addMouseListeners() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		popupMenu.add(deleteItem);
		
		deleteItem.addActionListener(_ -> {
			Long taskId = (Long) getClientProperty("task_id");
			if (new DeleteReminderService().execute(taskId)) {
				Main.getMain().getRemindersButton().doClick();
			} else {
				JOptionPane.showMessageDialog(this, "Failed to delete the reminder.");
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) { // Left click
					Long taskId = (Long) getClientProperty("task_id");
					Task task = new GetTaskByIdService().execute(taskId);
					if (task != null) {
						new CreateUpdateTaskWindow(Main.getMain(), task.project_id(), true, ReminderRowPanel.this);
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) { // Right click
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
}
