package main.gui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.lgooddatepicker.zinternaltools.WrapLayout;

import main.entities.IconColor;
import main.entities.Reminder;
import main.entities.Tag;
import main.gui.Main;
import main.gui.panels.ProjectInfoPanel;
import main.gui.popup.ErrorDialog;
import main.services.icon.GetIconColorOfTagService;
import main.services.reminder.GetReminderByIdService;
import main.services.tag.GetTagsOfTaskService;
import main.services.task.DeleteTaskService;

public class TaskWindow extends JDialog {
	
	private static final long serialVersionUID = 1L;
 	private JPanel contentPanel;
 	private JPanel source;
	private Timestamp selectedReminderTime = null;
	private String selectedReminderMsg = null;
	private static final Logger logger = Logger.getLogger(TaskWindow.class.getName());

	public TaskWindow(JPanel source, JPanel taskPanel) {
		logger.info("Drawing the window.");
		this.source = source;
		setTitle("Task Details");
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Window activeWindow = Main.main;

		if (activeWindow != null) {
			Dimension size = activeWindow.getSize();
			int w = Math.min(480, (int) (size.getWidth() * 0.75));
			int h = Math.min(400, (int) (size.getHeight() * 0.75));
			setSize(new Dimension(w, h));
			setLocationRelativeTo(activeWindow);
		} else {
			setSize(new Dimension(480, 400));
			setLocation(200, 200);
		}

		// Content Panel with standard margin (15, 15) and border 20 like CreateUpdateTaskWindow
		contentPanel = new JPanel(new BorderLayout(15, 15));
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// Extract properties
		Integer statusId = (Integer) taskPanel.getClientProperty("status_id");
		Integer priority = (Integer) taskPanel.getClientProperty("priority");
		Timestamp dueDate = (Timestamp) taskPanel.getClientProperty("due_date");
		Timestamp completedAt = (Timestamp) taskPanel.getClientProperty("completed_at");
		String taskTitle = (String)taskPanel.getClientProperty("task_title");
		Integer taskId = (Integer) taskPanel.getClientProperty("task_id");
		if (taskId != null) {
			getReminders(taskId);
		}
		
		// 1. Task Title (Header Panel) - Styled like titleField in CreateUpdateTaskWindow
		JTextField titleField = new JTextField(taskTitle);
		titleField.setEditable(false);
//		set caret invisible
		titleField.setCaretColor(new Color(0, 0,0,0));
		titleField.setFont(new Font("Dialog", Font.BOLD, 15));
		titleField.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		contentPanel.add(titleField, BorderLayout.NORTH);

		// 2. Center Panel (Description + Options) - Styled like centerPanel in CreateUpdateTaskWindow
		JPanel centerPanel = new JPanel(new BorderLayout(0, 12));

		String description = (String) taskPanel.getClientProperty("description");
		JTextArea descTextArea = new JTextArea();
		descTextArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		descTextArea.setLineWrap(true);
		descTextArea.setWrapStyleWord(true);
		descTextArea.putClientProperty("JTextArea.placeholderText", "Add details or description...");
		descTextArea.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		descTextArea.setText(description != null ? description : "");
		descTextArea.setEditable(false);
		descTextArea.setCaretColor(new Color(0, 0,0,0));
		
		JScrollPane descScrollPane = new JScrollPane(descTextArea);
		centerPanel.add(descScrollPane, BorderLayout.CENTER);

		// Format Detail values as clean string status/priority labels
		String statusStr = "Unknown";
		Color statusColor = Color.BLACK;
		if (statusId != null) {
			if (statusId == 1) {
				statusStr = "ACTIVE";
				statusColor = new Color(40, 167, 69);
			} else if (statusId == 2) {
				statusStr = "COMPLETED";
				statusColor = new Color(0, 123, 255);
			} else if (statusId == 3) {
				statusStr = "PAST";
				statusColor = new Color(108, 117, 125);
			} else {
				statusStr = "Status " + statusId;
			}
		}

		String priorityStr = "None";
		Color priorityColor = Color.BLACK;
		if (priority != null) {
			if (priority == 1) {
				priorityStr = "High";
				priorityColor = new Color(220, 53, 69);
			} else if (priority == 2) {
				priorityStr = "Medium";
				priorityColor = new Color(255, 140, 0);
			} else if (priority == 3) {
				priorityStr = "Low";
				priorityColor = new Color(40, 167, 69);
			} else {
				priorityStr = priority.toString();
			}
		}

		String dueDateStr = (dueDate != null ? dueDate.toString() : "No due date");
		Color dueDateColor = dueDate != null ? new Color(42, 157, 143) : null;

		// Options Panel under description (for Status, Priority, Due Date badges)
		JPanel optionsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 8, 0));
		optionsPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

		JButton statusBtn = new JButton(statusStr);
		statusBtn.setFocusable(false);
		statusBtn.putClientProperty("JButton.buttonType", "roundRect");
		if (statusColor != null) {
			statusBtn.setForeground(statusColor);
		}

		JButton priorityBtn = new JButton(priorityStr);
		priorityBtn.setFocusable(false);
		priorityBtn.putClientProperty("JButton.buttonType", "roundRect");
		if (priorityColor != null) {
			priorityBtn.setForeground(priorityColor);
		}

		JButton dueDateBtn = new JButton(dueDateStr);
		dueDateBtn.setFocusable(false);
		dueDateBtn.putClientProperty("JButton.buttonType", "roundRect");
		if (dueDateColor != null) {
			dueDateBtn.setForeground(dueDateColor);
		}

		optionsPanel.add(statusBtn);
		optionsPanel.add(priorityBtn);
		optionsPanel.add(dueDateBtn);

		if (completedAt != null) {
			JButton completedBtn = new JButton(completedAt.toString());
			completedBtn.setFocusable(false);
			completedBtn.putClientProperty("JButton.buttonType", "roundRect");
			completedBtn.setForeground(new Color(59, 130, 246));
			optionsPanel.add(completedBtn);
		}

		// Reminder display
		if (selectedReminderTime != null) {
			JButton reminderBtn = new JButton();
			reminderBtn.setFocusable(false);
			reminderBtn.putClientProperty("JButton.buttonType", "roundRect");
			LocalDateTime ldt = selectedReminderTime.toLocalDateTime();
			reminderBtn.setText("Remind: " + ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
			reminderBtn.setForeground(new Color(59, 130, 246));
			if (selectedReminderMsg != null && !selectedReminderMsg.isEmpty()) {
				reminderBtn.setToolTipText(selectedReminderMsg);
			}
			optionsPanel.add(reminderBtn);
		}

		// Fetch and display tags in a JMenu
		if (taskId != null) {
			List<Tag> tags = GetTagsOfTaskService.execute(taskId);
			if (tags != null && !tags.isEmpty()) {
				JButton tagsMenuButton = new JButton("Tags");
				JPopupMenu tagsMenu = new JPopupMenu();
				
				for (Tag tag : tags) {
					JMenuItem tagItem = new JMenuItem(tag.tag_name());
					
					IconColor ic = GetIconColorOfTagService.execute(tag.tag_id());
					Color tagColor = (ic == null) ? Color.GRAY : new Color(ic.red(), ic.green(), ic.blue());
					tagItem.setForeground(tagColor);
					
					tagsMenu.add(tagItem);
				}
				
				tagsMenuButton.addActionListener(_->tagsMenu.show(tagsMenuButton, 0, -tagsMenuButton.getHeight()));
				optionsPanel.add(tagsMenuButton);
			}
		}

		centerPanel.add(optionsPanel, BorderLayout.SOUTH);
		contentPanel.add(centerPanel, BorderLayout.CENTER);

		// 3. Footer Panel (Close button)
		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		JButton deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font("Dialog", Font.BOLD, 14));
		deleteButton.putClientProperty("JButton.buttonType", "roundRect");
		deleteButton.setForeground(new Color(220, 53, 69));
		addDeleteButtonActionListener(deleteButton, taskId);

		JButton closeButton = new JButton("Close");
		closeButton.setFont(new Font("Dialog", Font.BOLD, 14));
		closeButton.putClientProperty("JButton.buttonType", "roundRect");
		closeButton.addActionListener(_ -> dispose());

		buttonPane.add(deleteButton);
		buttonPane.add(closeButton);
		footerPanel.add(buttonPane, BorderLayout.EAST);
		contentPanel.add(footerPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(closeButton);

		revalidate();
		repaint();
		setVisible(true);

		logger.info("Window is ready.");
	}
	
	private void addDeleteButtonActionListener(JButton button, int taskId) {
		button.addActionListener(_->{
			int confirm = JOptionPane.showConfirmDialog(
					TaskWindow.this,
					"Are you sure you want to delete this task?",
					"Delete Task",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
				);
				if (confirm == JOptionPane.YES_OPTION) {
					if (DeleteTaskService.execute(taskId)) {
//						refreshing both gui
						if(source instanceof ProjectInfoPanel) {
							((ProjectInfoPanel) source).listTasks();
						}
						source.revalidate();
						Main.main.refreshWindow();
						dispose();
					} else {
						new ErrorDialog("Error", "Failed to delete the task.");
					}
				}
			});
	}
	
	private void getReminders(int taskId) {
		// Fetch existing reminder if any
		if (taskId != 0) {
			Reminder reminders = GetReminderByIdService.execute(taskId);
			if (reminders != null) {
				selectedReminderTime = reminders.remind_at();
				selectedReminderMsg = reminders.cstm_message();
			}
		}
	}
}
