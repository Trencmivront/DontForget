package main.java.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.custom.SpringContext;
import org.springframework.http.ResponseEntity;

import main.java.controllers.TaskController;
import main.java.entities.Task;
import main.java.gui.Main;
import main.java.gui.windows.CreateUpdateTaskWindow;

public class ProjectInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JScrollPane infoScrollPane;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectInfoPanel.class.getName());
	private ProjectRowPanel projectPanel;
	private static ProjectInfoPanel projectInfoPanel;
	private final Main main = Main.getMain();
	private TaskController taskController;
	
	public static ProjectInfoPanel getProjectInfoPanel() {
		return projectInfoPanel;
	}
	
	public ProjectInfoPanel(TaskController taskController) {
		this.taskController = taskController;
	}
	
	public ProjectInfoPanel(ProjectRowPanel panel) {
		if(projectInfoPanel != null) {
			projectInfoPanel = null;
		}
		projectInfoPanel = this;
		
		projectPanel = panel;
		this.taskController = SpringContext.getBean(TaskController.class);
		
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
	
	public void listTasks() {
		Long id = (Long)projectPanel.getClientProperty("projectId");
		
		ResponseEntity<List<Task>> tasksResponseEntity = taskController.getTasksOfProject(id);
		
		List<Task> tasks = tasksResponseEntity.getBody();
		
		if(tasks == null || tasks.isEmpty()) {
			infoScrollPane.removeAll();
			add(new EmptyPanel("No task found for this project."), BorderLayout.CENTER);
			revalidate();
			repaint();
			main.refreshWindow();
			logger.info("No task found for project.");
			return;
		}
		
		JPanel tasksContainer = new JPanel();
		tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));		
		
		tasks.forEach(task -> {
			tasksContainer.add(new TaskRowPanel(task, main));
		});
		
		infoScrollPane.setViewportView(tasksContainer);
		infoScrollPane.revalidate();
		infoScrollPane.repaint();
		main.refreshWindow();
	}
	
	private JPanel createHeaderPanel() {
		String title = (String)projectPanel.getClientProperty("projectTitle");
		String description = (String)projectPanel.getClientProperty("description");				
		return new HeaderPanel(title, description);
	}
	
	private void createTaskActionButton(JPanel panel) {
		JButton button = new JButton("+");
		
		button.setToolTipText("Create New Task");
		button.putClientProperty("JButton.buttonType", "roundRect");
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setFont(new Font("Ariel", 1, 20));
		
		button.addActionListener(_-> new CreateUpdateTaskWindow(Main.getMain() ,(Long)projectPanel.getClientProperty("projectId"), false, null));
		button.setMaximumSize(new Dimension(40, 40));
		
		panel.add(button, BorderLayout.EAST);
	}
	
	private void createDeleteCompletedTasksButton(JPanel panel) {
		JButton button = new JButton("DC");
		
		button.setToolTipText("Delete Completed Task(s)");
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
				Long id = (Long) projectPanel.getClientProperty("projectId");
				taskController.deleteTask(id);
				listTasks();
			}
		});
	}
}
