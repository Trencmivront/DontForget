package main.io.github.trencmivront.dontforget.gui.panels;

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
import org.springframework.http.ResponseEntity;

import main.io.github.trencmivront.dontforget.controllers.TaskController;
import main.io.github.trencmivront.dontforget.custom.SpringContext;
import main.io.github.trencmivront.dontforget.dto.ProjectDTO;
import main.io.github.trencmivront.dontforget.dto.TaskDTO;
import main.io.github.trencmivront.dontforget.gui.Main;
import main.io.github.trencmivront.dontforget.gui.panels.rows.TaskRowPanel;
import main.io.github.trencmivront.dontforget.gui.windows.TaskWindow;

public class ProjectInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JScrollPane infoScrollPane;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectInfoPanel.class.getName());
	private ProjectDTO projectDTO;
	private static ProjectInfoPanel projectInfoPanel;
	private final Main main = Main.getMain();
	private final TaskController taskController = SpringContext.getBean(TaskController.class);
	
	public static ProjectInfoPanel getProjectInfoPanel() {
		return projectInfoPanel;
	}
	
	public ProjectInfoPanel(ProjectDTO projectDTO) {
		if(projectInfoPanel != null) {
			projectInfoPanel = null;
		}
		projectInfoPanel = this;
		
		this.projectDTO = projectDTO;
		
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
		Long id = projectDTO.getProjectId();
		
		ResponseEntity<List<TaskDTO>> tasksResponseEntity = taskController.getTasksOfProject(id);
		
		List<TaskDTO> tasks = tasksResponseEntity.getBody();
		
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
			tasksContainer.add(new TaskRowPanel(task));
		});
		
		infoScrollPane.setViewportView(tasksContainer);
		infoScrollPane.revalidate();
		infoScrollPane.repaint();
		main.refreshWindow();
	}
	
	private JPanel createHeaderPanel() {
		String title = projectDTO.getProjectTitle();
		String description = projectDTO.getDescription();				
		return new HeaderPanel(title, description);
	}
	
	private void createTaskActionButton(JPanel panel) {
		JButton button = new JButton("+");
		
		button.setToolTipText("Create New Task");
		button.putClientProperty("JButton.buttonType", "roundRect");
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setFont(new Font("Ariel", 1, 20));
		
		button.addActionListener(_-> new TaskWindow(null, projectDTO.getProjectId()));
		
		button.setMaximumSize(new Dimension(40, 40));
		
		panel.add(button, BorderLayout.EAST);
	}
	
	private void createDeleteCompletedTasksButton(JPanel panel) {
		JButton button = new JButton("Del Completed");
		
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
//				and I was wondering why this won't work. It turns out our AI firend made some changes here and
//				replaced deleteCompletedTasks with deleteTask, like how smart it is.
				taskController.deleteCompletedTasks(projectDTO.getProjectId());
				listTasks();
			}
		});
	}
}
