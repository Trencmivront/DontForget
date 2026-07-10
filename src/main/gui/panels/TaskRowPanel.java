package main.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.entities.Task;
import main.gui.Main;
import main.gui.windows.CreateUpdateTaskWindow;
import main.services.task.UpdateTaskService;

public class TaskRowPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(TaskRowPanel.class.getName());

//	we take panel in case it is ProjectInfoPanel and we need to refresh it
//	im doing tons of bullsht rn
	public TaskRowPanel(Task task) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
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
		
		JCheckBox chk = new JCheckBox();
		JLabel title = new JLabel(task.task_title());
//		means that task is completed
		
		switch (task.status_id() != null ? task.status_id().intValue() : 1) {
		case 1, 0:
		break;
		case 2:	{title.setText("<html><i style='color: gray;'><s>" + task.task_title() + "</i></s></html>");
		chk.setSelected(true);}
			break;
		case 3: {title.setText("<html><i style='color: rgb(94, 75, 39);'>" + task.task_title() + "</i></html>");
		chk.setEnabled(false);}
			break;
		default:
			logger.warning("Task Status id is invalid.");
			break;
		}
		
		chk.setBorderPainted(true);

		switch(task.priority()) {
		case 1: chk.setBorder(new LineBorder(Color.RED, 1, true));
		break;
		case 2: chk.setBorder(new LineBorder(Color.ORANGE, 1, true));
		break;
		case 3: chk.setBorder(new LineBorder(Color.GREEN, 1, true));
		break;
			default: chk.setBorder(new LineBorder(Color.GRAY, 1, true));
			break;
		}
		
		addCheckBoxEventListener(chk);
		
		add(chk);
		add(title);
		
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		addTaskTitleActionListener(title);
		
	}
	
	private void addTaskTitleActionListener(JLabel title) {
		title.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new CreateUpdateTaskWindow(Main.getMain(), (Long) getClientProperty("project_id"), true, TaskRowPanel.this);
			}
		});
	}
	
//	we want to update task as completed when the checkbox is selected
	private void addCheckBoxEventListener(JCheckBox chk) {
		chk.addActionListener(_->{
			JPanel parentContainer = (JPanel)chk.getParent();
			if (parentContainer == null) {
				return;
			}
			
			Long taskId = (Long) parentContainer.getClientProperty("task_id");
			String taskTitle = (String) parentContainer.getClientProperty("task_title");
			String description = (String) parentContainer.getClientProperty("description");
			Integer priority = (Integer) parentContainer.getClientProperty("priority");
			Timestamp dueDate = (Timestamp) parentContainer.getClientProperty("due_date");
			Integer listOrder = (Integer) parentContainer.getClientProperty("list_order");
			Long projectId = (Long) parentContainer.getClientProperty("project_id");
			Timestamp createdAt = (Timestamp) parentContainer.getClientProperty("created_at");

			Long newStatusId = 1L;
			Timestamp completedAt = null;
			if (chk.isSelected()) {
				newStatusId = 2L; // COMPLETED
				completedAt = new Timestamp(System.currentTimeMillis());
				dueDate = null;
			} else {
				if (dueDate != null && dueDate.toLocalDateTime().toLocalDate().isBefore(LocalDate.now())) {
					newStatusId = 3L; // PAST
				} else {
					newStatusId = 1L; // ACTIVE
				}
			}

			Task updatedTask = new Task(
				taskId,
				taskTitle,
				description,
				newStatusId,
				priority,
				dueDate,
				listOrder,
				projectId,
				createdAt,
				new Timestamp(System.currentTimeMillis()),
				completedAt
			);
			
			if (UpdateTaskService.execute(updatedTask)) {
				ProjectInfoPanel.getProjectInfoPanel().listTasks();
			}
		});
	}
	
}
