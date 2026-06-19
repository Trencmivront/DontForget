package app.gui.panels;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import app.entities.Task;
import app.services.task.GetTodaysTasksService;

public class TodayPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	
	public TodayPanel() {
		
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
		
		List<Task> tasks = GetTodaysTasksService.execute();
		if(tasks.isEmpty()) {
			add(new EmptyPanel("No task for today."));
		}
		
	}

}
