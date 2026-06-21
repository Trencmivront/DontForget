package main.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import main.entities.Task;
import main.gui.Main;
import main.gui.windows.CreateTaskWindow;
import main.gui.windows.TaskWindow;
import main.services.task.DeleteCompletedTasksService;
import main.services.task.GetTasksOfProjectService;
import main.services.task.UpdateTaskService;

public class ProjectInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JScrollPane infoScrollPane;
	
	private static final Logger logger = Logger.getLogger(ProjectInfoPanel.class.getName());
	private JPanel projectPanel;
	
	public ProjectInfoPanel(JPanel panel) {
		projectPanel = panel;
		setLayout(new BorderLayout());
		
		add(createHeaderPanel(), BorderLayout.NORTH);
		
		infoScrollPane = new JScrollPane();
		infoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(infoScrollPane, BorderLayout.CENTER);
		
		JPanel taskActionsPanel = new JPanel();
		taskActionsPanel.setLayout(new BorderLayout());
		add(taskActionsPanel, BorderLayout.SOUTH);
		createTaskActionButton(taskActionsPanel);
		createDeleteCompletedTasksButton(taskActionsPanel);
		
		listTasks();
		
	}
	
	private void listTasks() {
		int id = (int)projectPanel.getClientProperty("project_id");
		List<Task> tasks = GetTasksOfProjectService.execute(id);
		
		if(tasks.isEmpty()) {
			infoScrollPane.removeAll();
			add(new EmptyPanel("No task found for this project."), BorderLayout.CENTER);
			revalidate();
			repaint();
			Main.refreshWindow();
			logger.info("No task found for project.");
			return;
		}
		ListIterator<Task> i = tasks.listIterator();
				
		JPanel info = new JPanel();
		info.setLayout(new BorderLayout());
		
		JPanel tasksContainer = new JPanel();
		tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));		
		
		while(i.hasNext()) {
			tasksContainer.add(createTaskContainer(i.next()));
		}
		
		infoScrollPane.setViewportView(tasksContainer);
		infoScrollPane.revalidate();
		infoScrollPane.repaint();
	}
	
	private JPanel createHeaderPanel() {
		String title = (String)projectPanel.getClientProperty("project_title");
		String description = (String)projectPanel.getClientProperty("description");				
		return new HeaderPanel(title, description);
	}
	
	private void createTaskActionButton(JPanel panel) {
		JButton button = new JButton("+");
		
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setFont(new Font("Ariel", 1, 20));
		
		button.addActionListener(_-> new CreateTaskWindow(projectPanel));
		button.setBorder(new EmptyBorder(5, 0, 5, 0));
		button.setMaximumSize(new Dimension(40, 40));
		
		panel.add(button, BorderLayout.EAST);
	}
	
	private void createDeleteCompletedTasksButton(JPanel panel) {
		JButton button = new JButton("DC");
		
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setFont(new Font("Ariel", 1, 14));
		
		addDeleteCompletedTasksActionListener(button);
		button.setBorder(new EmptyBorder(5, 0, 5, 0));
		button.setMaximumSize(new Dimension(40, 40));
		
		panel.add(button, BorderLayout.WEST);
	}
	
	private void addDeleteCompletedTasksActionListener(JButton button) {
		button.addActionListener(_ -> {
			int confirm = JOptionPane.showConfirmDialog(
					ProjectInfoPanel.this,
					"Are you sure you want to delete completed tasks?",
					"Delete Completed Tasks",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
				);
			if(confirm == JOptionPane.YES_OPTION) {
				int id = (int) projectPanel.getClientProperty("project_id");
				DeleteCompletedTasksService.execute(id);
				listTasks();
			}
		});
	}
	
	private JPanel createTaskContainer(Task task){
		
		JPanel taskPanel = new JPanel();
		taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
		
		taskPanel.putClientProperty("task_id", task.task_id());
		taskPanel.putClientProperty("task_title", task.task_title());
		taskPanel.putClientProperty("description", task.description());
		taskPanel.putClientProperty("status_id", task.status_id());
		taskPanel.putClientProperty("priority", task.priority());
		taskPanel.putClientProperty("due_date", task.due_date());
		taskPanel.putClientProperty("list_order", task.list_order());
		taskPanel.putClientProperty("project_id", task.project_id());
		taskPanel.putClientProperty("created_at", task.created_at());
		taskPanel.putClientProperty("updated_at", task.updated_at());
		taskPanel.putClientProperty("completed_at", task.completed_at());
		
		JCheckBox chk = new JCheckBox();
		JLabel title = new JLabel(task.task_title());
//		means that task is completed
		
		switch (task.status_id()) {
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
		
		taskPanel.add(chk);
		taskPanel.add(title);
		
		taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		taskPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new TaskWindow(ProjectInfoPanel.this ,taskPanel);
			}
		});
		return taskPanel;
	}
	
//	we want to update task as completed when the checkbox is selected
	private void addCheckBoxEventListener(JCheckBox chk) {
		chk.addActionListener(_->{
			JPanel parentContainer = (JPanel)chk.getParent();
			if (parentContainer == null) {
				return;
			}
			
			Integer taskId = (Integer) parentContainer.getClientProperty("task_id");
			String taskTitle = (String) parentContainer.getClientProperty("task_title");
			String description = (String) parentContainer.getClientProperty("description");
			Integer priority = (Integer) parentContainer.getClientProperty("priority");
			Timestamp dueDate = (Timestamp) parentContainer.getClientProperty("due_date");
			Integer listOrder = (Integer) parentContainer.getClientProperty("list_order");
			Integer projectId = (Integer) parentContainer.getClientProperty("project_id");
			Timestamp createdAt = (Timestamp) parentContainer.getClientProperty("created_at");

			int newStatusId = 1;
			Timestamp completedAt = null;
			if (chk.isSelected()) {
				newStatusId = 2; // COMPLETED
				completedAt = new Timestamp(System.currentTimeMillis());
			} else {
				if (dueDate != null && dueDate.toLocalDateTime().toLocalDate().isBefore(LocalDate.now())) {
					newStatusId = 3; // PAST
				} else {
					newStatusId = 1; // ACTIVE
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
				listTasks();
			}
		});
	}

}
