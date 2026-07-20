package main.java.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.api.Api;
import main.java.entities.Task;
import main.java.gui.Main;
import main.java.gui.windows.CreateUpdateTaskWindow;

public class TaskRowPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(TaskRowPanel.class.getName());
	private final Api api = new Api();
	private final ObjectMapper mapper = new ObjectMapper();

//	we take panel in case it is ProjectInfoPanel and we need to refresh it
//	im doing tons of bullsht rn
	public TaskRowPanel(Task task) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		putClientProperty("taskId", task.taskId());
		putClientProperty("taskTitle", task.taskTitle());
		putClientProperty("description", task.description());
		putClientProperty("statusId", task.statusId());
		putClientProperty("priority", task.priority());
		putClientProperty("dueDate", task.dueDate());
		putClientProperty("listOrder", task.listOrder());
		putClientProperty("projectId", task.projectId());
		putClientProperty("createdAt", task.createdAt());
		putClientProperty("updatedAt", task.updatedAt());
		putClientProperty("completedAt", task.completedAt());
		
		setToolTipText(String.format("Show: %s", task.taskTitle()));;
		
		JCheckBox chk = new JCheckBox();
		JLabel title = new JLabel(task.taskTitle());
//		means that task is completed
		
		switch (task.statusId() != null ? task.statusId().intValue() : 1) {
		case 1, 0:
		break;
		case 2:	{title.setText("<html><i style='color: gray;'><s>" + task.taskTitle() + "</i></s></html>");
		chk.setSelected(true);}
			break;
		case 3: {title.setText("<html><i style='color: rgb(94, 75, 39);'>" + task.taskTitle() + "</i></html>");
		chk.setEnabled(false);}
			break;
		default:
			logger.warn("Task Status id is invalid.");
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
		addTaskActionListener();
		
	}
	
	private void addTaskActionListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					new CreateUpdateTaskWindow(Main.getMain(), (Long) getClientProperty("projectId"), true, TaskRowPanel.this);
				}
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
			
			Long taskId = (Long) parentContainer.getClientProperty("taskId");
			String taskTitle = (String) parentContainer.getClientProperty("taskTitle");
			String description = (String) parentContainer.getClientProperty("description");
			Integer priority = (Integer) parentContainer.getClientProperty("priority");
			Timestamp dueDate = (Timestamp) parentContainer.getClientProperty("dueDate");
			Integer listOrder = (Integer) parentContainer.getClientProperty("listOrder");
			Long projectId = (Long) parentContainer.getClientProperty("projectId");
			Timestamp createdAt = (Timestamp) parentContainer.getClientProperty("createdAt");

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
			
			try {
				String body = mapper.writeValueAsString(updatedTask);
				int code = api.put("/api/task/update", body, null);
				if (code < 400) {
					ProjectInfoPanel.getProjectInfoPanel().listTasks();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
}
