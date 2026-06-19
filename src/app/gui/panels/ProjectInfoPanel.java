package app.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import app.entities.Task;
import app.gui.windows.CreateTaskWindow;
import app.gui.windows.TaskWindow;
import app.services.task.GetTasksOfProjectService;

public class ProjectInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JScrollPane infoScrollPane;
	
	private static final Logger logger = Logger.getLogger(ProjectInfoPanel.class.getName());
	
	public ProjectInfoPanel(JPanel panel) {
				
		setLayout(new BorderLayout());
		
		add(createHeaderPanel(panel), BorderLayout.NORTH);
		
		infoScrollPane = new JScrollPane();
		infoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(infoScrollPane);
		
		JPanel taskActionsPanel = new JPanel();
		taskActionsPanel.setLayout(new BorderLayout());
		add(taskActionsPanel, BorderLayout.SOUTH);
		createTaskActionButton(taskActionsPanel);
		
		listTasks(panel);
		
	}
	
	private void listTasks(JPanel projectPanel) {
		int id = (int)projectPanel.getClientProperty("project_id");
		List<Task> tasks = GetTasksOfProjectService.execute(id);
		if(tasks.isEmpty()) {
			add(new EmptyPanel("No task found for this project."), BorderLayout.CENTER);
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
	
	private JPanel createHeaderPanel(JPanel projectPanel) {
		String title = (String)projectPanel.getClientProperty("project_title");
		String description = (String)projectPanel.getClientProperty("description");				
		return new HeaderPanel(title, description);
	}
	
	private void createTaskActionButton(JPanel panel) {
		JButton button = new JButton("+");
		
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setFont(new Font("Ariel", 1, 20));
		
		button.addActionListener(_->{
			new CreateTaskWindow();
		});
		button.setBorder(new EmptyBorder(5, 0, 5, 0));
		button.setMaximumSize(new Dimension(40, 40));
		
		panel.add(button, BorderLayout.EAST);
	}
	
	private JPanel createTaskContainer(Task task){
		
		JPanel taskPanel = new JPanel();
		taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
		JLabel title = new JLabel(task.task_title());
		
		taskPanel.putClientProperty("task_id", task.task_id());
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
}
