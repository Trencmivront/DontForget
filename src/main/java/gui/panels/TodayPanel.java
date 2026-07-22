package main.java.gui.panels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import main.java.custom.SpringContext;
import main.java.controllers.TaskController;
import org.springframework.http.ResponseEntity;
import main.java.entities.Task;
import main.java.gui.Main;

public class TodayPanel extends JPanel{

	private static final Logger logger = LoggerFactory.getLogger(TodayPanel.class.getName());
	
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private final Main main = Main.getMain();
	private TaskController taskController;

	public TodayPanel() {
		logger.info("Initializing TodayPanel");
		this.taskController = SpringContext.getBean(TaskController.class);
		
		setLayout(new BorderLayout());
		
		add(new HeaderPanel("Today's Tasks"), BorderLayout.NORTH);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
		
		JPanel taskActionsPanel = new JPanel();
		add(taskActionsPanel, BorderLayout.SOUTH);
		
		listTasks();
	}
	
	private void listTasks(){
		List<Task> tasks = Collections.emptyList();
		try {
			ResponseEntity<List<Task>> res = taskController.getTodaysTasks();
			tasks = res.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(tasks == null || tasks.isEmpty()) {
			scrollPane.setViewportView(new EmptyPanel("No task for today."));
			return;
		}
				
		ListIterator<Task> i = tasks.listIterator();
		
		JPanel tasksContainer = new JPanel();
		tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));		
		
		while(i.hasNext()) {
			tasksContainer.add(new TaskRowPanel(i.next(), main));
		}
		
		scrollPane.setViewportView(tasksContainer);
		scrollPane.revalidate();
		scrollPane.repaint();
		refresh();
		main.refreshWindow();
	}
	
	private void refresh() {
		revalidate();
		repaint();
	}
}
