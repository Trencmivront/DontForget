package main.gui.panels;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.entities.Reminder;
import main.entities.Task;

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
		add(title, BorderLayout.CENTER);
	}
}
