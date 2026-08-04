package main.io.github.trencmivront.dontforget.gui.panels.rows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import main.io.github.trencmivront.dontforget.controllers.TaskController;
import main.io.github.trencmivront.dontforget.custom.SpringContext;
import main.io.github.trencmivront.dontforget.dto.TaskDTO;
import main.io.github.trencmivront.dontforget.gui.panels.ProjectInfoPanel;
import main.io.github.trencmivront.dontforget.gui.windows.TaskWindow;

public class TaskRowPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(TaskRowPanel.class.getName());
	private final TaskController taskController = SpringContext.getBean(TaskController.class);
	
	private TaskDTO taskDTO;

//	we take panel in case it is ProjectInfoPanel and we need to refresh it
//	im doing tons of bullsht rn
	public TaskRowPanel(TaskDTO task) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.taskDTO = task;
		
		setToolTipText(String.format("Show: %s", task.getTaskTitle()));
		
		JCheckBox chk = new JCheckBox();
		JLabel title = new JLabel(task.getTaskTitle());
//		means that task is completed
		
		switch (task.getStatusId() != null ? task.getStatusId().intValue() : 1) {
		case 1, 0:
		break;
		case 2:	{title.setText("<html><i style='color: gray;'><s>" + task.getTaskTitle() + "</i></s></html>");
		chk.setSelected(true);}
			break;
		case 3: {title.setText("<html><i style='color: rgb(94, 75, 39);'>" + task.getTaskTitle() + "</i></html>");
		chk.setEnabled(false);}
			break;
		default:
			logger.warn("Task Status id is invalid.");
			break;
		}
		
		chk.setBorderPainted(true);
//		if it is null, we insert 0
		switch(task.getPriority() == null ? 0:task.getPriority()) {
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
					new TaskWindow(taskDTO.getTaskId(), null);
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
			
			Long taskId = taskDTO.getTaskId();
			String taskTitle = taskDTO.getTaskTitle();
			String description = taskDTO.getDescription();
			Integer priority = taskDTO.getPriority();
			LocalDate dueDate = taskDTO.getDueDate();
			Long projectId = taskDTO.getProjectId();

			Long newStatusId = 1L;
			if (chk.isSelected()) {
				newStatusId = 2L; // COMPLETED
				dueDate = null;
			} else {
				if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
					newStatusId = 3L; // PAST
				} else {
					newStatusId = 1L; // ACTIVE
				}
			}

			TaskDTO updatedTask = new TaskDTO(
				taskId,
				taskTitle,
				description,
				newStatusId,
				priority,
				dueDate != null ? dueDate : null,
				projectId
			);
			
			try {
				ResponseEntity<String> response = taskController.updateTask(updatedTask);
				if (response.getStatusCode().is2xxSuccessful()) {
					ProjectInfoPanel.getProjectInfoPanel().listTasks();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
}
