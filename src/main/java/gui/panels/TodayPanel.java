package main.java.gui.panels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.api.Api;
import main.java.entities.Task;
import main.java.gui.Main;

@Component
public class TodayPanel extends JPanel{

	private static final Logger logger = LoggerFactory.getLogger(TodayPanel.class.getName());
	
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private final Main main = Main.getMain();
	private final Api api = new Api();
	private final ObjectMapper mapper = new ObjectMapper();

	public TodayPanel() {
		logger.info("Initializing TodayPanel");
		
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
			String res = api.get("/api/task/today");
			tasks = mapper.readValue(res, new TypeReference<List<Task>>() {});
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
