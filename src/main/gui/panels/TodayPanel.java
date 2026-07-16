package main.gui.panels;

import java.awt.BorderLayout;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import main.entities.Task;
import main.gui.Main;
import main.services.task.GetTodaysTasksService;

public class TodayPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private final Main main = Main.getMain();
	
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
		List<Task> tasks = new GetTodaysTasksService().execute();
		
		if(tasks.isEmpty()) {
			scrollPane.setViewportView(new EmptyPanel("No task for today."));
			return;
		}
				
		ListIterator<Task> i = tasks.listIterator();
		
		JPanel tasksContainer = new JPanel();
		tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));		
		
		while(i.hasNext()) {
			tasksContainer.add(new TaskRowPanel(i.next()));
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
