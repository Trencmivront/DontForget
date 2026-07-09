package main.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import main.entities.Task;
import main.gui.Main;
import main.gui.windows.CreateUpdateTaskWindow;
import main.services.task.DeleteCompletedTasksService;
import main.services.task.GetTasksOfProjectService;

public class ProjectInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JScrollPane infoScrollPane;
	
	private static final Logger logger = Logger.getLogger(ProjectInfoPanel.class.getName());
	private JPanel projectPanel;
	private static ProjectInfoPanel projectInfoPanel;
	
	public static ProjectInfoPanel getProjectInfoPanel() {
		return projectInfoPanel;
	}
	
	public ProjectInfoPanel(JPanel panel) {
		projectPanel = panel;
		projectInfoPanel = this;
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
		Long id = (Long)projectPanel.getClientProperty("project_id");
		List<Task> tasks = GetTasksOfProjectService.execute(id);
		
		if(tasks.isEmpty()) {
			infoScrollPane.removeAll();
			add(new EmptyPanel("No task found for this project."), BorderLayout.CENTER);
			revalidate();
			repaint();
			Main.getMain().refreshWindow();
			logger.info("No task found for project.");
			return;
		}
		ListIterator<Task> i = tasks.listIterator();
				
		JPanel info = new JPanel();
		info.setLayout(new BorderLayout());
		
		JPanel tasksContainer = new JPanel();
		tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));		
		
		while(i.hasNext()) {
			tasksContainer.add(new TaskRowPanel(projectPanel, i.next()));
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
		
		button.putClientProperty("JButton.buttonType", "roundRect");
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setFont(new Font("Ariel", 1, 20));
		
		button.addActionListener(_-> new CreateUpdateTaskWindow(Main.getMain() ,(Long)projectPanel.getClientProperty("project_id"), false, null));
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
				Long id = (Long) projectPanel.getClientProperty("project_id");
				DeleteCompletedTasksService.execute(id);
				listTasks();
			}
		});
	}
}
