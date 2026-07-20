package main.java.gui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.zinternaltools.WrapLayout;

import main.java.api.Api;
import main.java.custom.CustomIcon;
import main.java.dco.TaskDCO;
import main.java.entities.IconColor;
import main.java.entities.Reminder;
import main.java.entities.Tag;
import main.java.entities.Task;
import main.java.entities.TaskTag;
import main.java.gui.Main;
import main.java.gui.panels.ProjectInfoPanel;
import main.java.gui.panels.ProjectRowPanel;
import main.java.gui.panels.ReminderRowPanel;
import main.java.gui.popups.ErrorDialog;
import main.java.gui.popups.ReminderDialog;

public class CreateUpdateTaskWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CreateUpdateTaskWindow.class.getName());

	private LocalDate selectedDueDate = null;
	private Integer selectedPriority = null;

	private Long projectId;
	private JPanel rowPanel;
	private JTextField titleField;
	private JTextArea descArea;
	private List<Tag> selectedTags = new ArrayList<>();

	private Timestamp selectedReminderTime = null;
	private String selectedReminderMsg = null;
	private boolean isRecurring = false;
	private List<DayOfWeek> selectedRecurringDays = new ArrayList<>();

	private JButton dueDateBtn;
	private JButton tagsBtn;
	private JButton priorityBtn;
	private JButton reminderBtn;
	private static final Main main = Main.getMain();

	private final Api api = new Api();
	private final ObjectMapper mapper = new ObjectMapper();

	public LocalDate getSelectedDueDate() {
		return selectedDueDate;
	}

	public void setSelectedDueDate(LocalDate selectedDueDate) {
		this.selectedDueDate = selectedDueDate;
	}

	public Integer getSelectedPriority() {
		return selectedPriority;
	}

	public void setSelectedPriority(Integer selectedPriority) {
		this.selectedPriority = selectedPriority;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public JTextField getTitleField() {
		return titleField;
	}

	public void setTitleField(JTextField titleField) {
		this.titleField = titleField;
	}

	public JTextArea getDescArea() {
		return descArea;
	}

	public void setDescArea(JTextArea descArea) {
		this.descArea = descArea;
	}

	public List<Tag> getSelectedTags() {
		return selectedTags;
	}

	public void setSelectedTags(List<Tag> selectedTags) {
		this.selectedTags = selectedTags;
	}

	public Timestamp getSelectedReminderTime() {
		return selectedReminderTime;
	}

	public void setSelectedReminderTime(Timestamp selectedReminderTime) {
		this.selectedReminderTime = selectedReminderTime;
	}

	public String getSelectedReminderMsg() {
		return selectedReminderMsg;
	}

	public void setSelectedReminderMsg(String selectedReminderMsg) {
		this.selectedReminderMsg = selectedReminderMsg;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public List<DayOfWeek> getSelectedRecurringDays() {
		return selectedRecurringDays;
	}

	public void setSelectedRecurringDays(List<DayOfWeek> selectedRecurringDays) {
		this.selectedRecurringDays = selectedRecurringDays;
	}

	public JButton getDueDateBtn() {
		return dueDateBtn;
	}

	public void setDueDateBtn(JButton dueDateBtn) {
		this.dueDateBtn = dueDateBtn;
	}

	public CreateUpdateTaskWindow(JFrame source ,Long projectId, boolean isUpdate, JPanel rowPanel) {
		logger.info("Initializing CreateUpdateTaskWindow.");
		super(source, isUpdate ? "Update Task" : "Create Task", false);
		this.projectId = projectId;
		this.rowPanel = rowPanel;
		
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Window activeWindow = Main.getMain();

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

		// Content Panel with standard margin
		JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// 1. Task Title (Header Panel)
		titleField = new JTextField();
		titleField.setFont(new Font("Dialog", Font.BOLD, 15));
		titleField.putClientProperty("JTextField.placeholderText", "Title of the task");
		titleField.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		contentPanel.add(titleField, BorderLayout.NORTH);

		// 2. Center Panel (Description + Options)
		JPanel centerPanel = new JPanel(new BorderLayout(0, 12));

		descArea = new JTextArea();
		descArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		descArea.putClientProperty("JTextArea.placeholderText", "Add details or description...");
		descArea.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));

		JScrollPane descScrollPane = new JScrollPane(descArea);
		centerPanel.add(descScrollPane, BorderLayout.CENTER);

		// Options bar under description
		JPanel optionsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 8, 0));
		optionsPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

		dueDateBtn = new JButton("Due Date");
		dueDateBtn.putClientProperty("JButton.buttonType", "roundRect");

		priorityBtn = new JButton("Priority");
		priorityBtn.putClientProperty("JButton.buttonType", "roundRect");

		reminderBtn = new JButton("Reminder");
		reminderBtn.putClientProperty("JButton.buttonType", "roundRect");

		tagsBtn = new JButton("Tags");
		tagsBtn.putClientProperty("JButton.buttonType", "roundRect");

		optionsPanel.add(dueDateBtn);
		optionsPanel.add(priorityBtn);
		optionsPanel.add(reminderBtn);
		optionsPanel.add(tagsBtn);

		JPanel southContainer = new JPanel();
		southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
		southContainer.add(optionsPanel);

		centerPanel.add(southContainer, BorderLayout.SOUTH);

		contentPanel.add(centerPanel, BorderLayout.CENTER);

		// 3. Footer Panel (Cancel + Create buttons)
		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		JButton createButton = new JButton(isUpdate ? "UPDATE" : "CREATE");
		createButton.setFont(new Font("Dialog", Font.BOLD, 14));
		createButton.putClientProperty("JButton.buttonType", "roundRect");

		buttonPane.add(createButton);
		footerPanel.add(buttonPane, BorderLayout.EAST);
		contentPanel.add(footerPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(createButton);
		
		addCreateButtonActionListener(createButton, isUpdate, rowPanel);

		// Set up dropdown popups for the option buttons
		setupDueDateMenu(dueDateBtn);
		setupPriorityMenu(priorityBtn);
		setupReminderMenu(reminderBtn);
		setupTagsDialog(tagsBtn);

		if (isUpdate && rowPanel != null) {
			String taskTitle = (String) rowPanel.getClientProperty("taskTitle");
			if (taskTitle != null) {
				titleField.setText(taskTitle);
			}
			String description = (String) rowPanel.getClientProperty("description");
			if (description != null) {
				descArea.setText(description);
			}
			
			Long taskId = (Long) rowPanel.getClientProperty("taskId");
			Timestamp dueDate = (Timestamp) rowPanel.getClientProperty("dueDate");
			Integer priority = (Integer) rowPanel.getClientProperty("priority");
			
			setDueDate(dueDate);
			setPriority(priority);
			if(taskId != null) {
				setReminder(taskId);
				setRecurringDays(taskId);
				setTag(taskId);
			}
			
		}
		
		revalidate();
		repaint();
		javax.swing.SwingUtilities.invokeLater(() -> setVisible(true));
		logger.info("CreateUpdateTaskWindow display complete.");
	}
	
	private void setDueDate(Timestamp dueDate) {
		if (dueDate != null) {
			selectedDueDate = dueDate.toLocalDateTime().toLocalDate();
			dueDateBtn.setText(selectedDueDate.toString());
			dueDateBtn.setForeground(new Color(42, 157, 143));
		}		
	}

	private void setTag(Long taskId) {
		List<Tag> tags = Collections.emptyList();
		try {
			String res = api.get("/api/tag/task/", taskId);
			tags = mapper.readValue(res, new TypeReference<List<Tag>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (tags != null && !tags.isEmpty()) {
			selectedTags.addAll(tags);
			updateTagsButton(tagsBtn);
		}
	}

	private void setRecurringDays(Long taskId) {
		List<DayOfWeek> recurringDays = Collections.emptyList();
		try {
			String res = api.get("/api/recurring-task/task/", taskId);
			recurringDays = mapper.readValue(res, new TypeReference<List<DayOfWeek>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (recurringDays != null && !recurringDays.isEmpty()) {
			selectedRecurringDays.addAll(recurringDays);
			isRecurring = true;
			dueDateBtn.setText("Disabled");
			dueDateBtn.setToolTipText("Can't set due date when\nrecurring task is enabled");
			dueDateBtn.setForeground(null);
			dueDateBtn.setEnabled(false);
		}
	}

	private void setReminder(Long taskId) {
		Reminder reminder = null;
		try {
			String res = api.get("/api/reminder/get/", taskId);
			reminder = mapper.readValue(res, Reminder.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (reminder != null) {
			selectedReminderTime = reminder.remindAt();
			selectedReminderMsg = reminder.message();
			if (selectedReminderTime != null) {
				LocalDateTime ldt = selectedReminderTime.toLocalDateTime();
				reminderBtn.setText("Remind: " + ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
				reminderBtn.setForeground(new Color(59, 130, 246));
			}
		}
	}

	private void setPriority(Integer priority) {
		if (priority != null) {
			selectedPriority = priority;
			if (priority == 1) {
				priorityBtn.setText("High");
				priorityBtn.setForeground(new Color(239, 68, 68));
			} else if (priority == 2) {
				priorityBtn.setText("Medium");
				priorityBtn.setForeground(new Color(245, 158, 11));
			} else if (priority == 3) {
				priorityBtn.setText("Low");
				priorityBtn.setForeground(new Color(16, 185, 129));
			}
		}
		
	}

	private void addCreateButtonActionListener(JButton button, boolean isUpdate, JPanel taskPanel) {
		button.addActionListener(_->{
			String title = titleField.getText().trim();
			String description = descArea.getText().trim();

			if (title.isEmpty()) {
				JOptionPane.showMessageDialog(CreateUpdateTaskWindow.this, "Task title cannot be empty.", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			};

			Long taskId = null;
			if (isUpdate) {
				taskId = (Long) taskPanel.getClientProperty("taskId");
				Long statusId = (Long) taskPanel.getClientProperty("statusId");
				Integer listOrder = (Integer) taskPanel.getClientProperty("listOrder");
				Timestamp createdAt = (Timestamp) taskPanel.getClientProperty("createdAt");
				Timestamp completedAt = (Timestamp) taskPanel.getClientProperty("completedAt");
				if (statusId == null) statusId = 1L;
				if (listOrder == null) listOrder = 1;
				if (createdAt == null) createdAt = new Timestamp(System.currentTimeMillis());

				Timestamp dueDateTimestamp = selectedDueDate != null ? Timestamp.valueOf(selectedDueDate.atStartOfDay()) : null;

				Task task = new Task(
					taskId,
					title,
					description,
					statusId,
					selectedPriority,
					dueDateTimestamp,
					listOrder,
					projectId,
					createdAt,
					new Timestamp(System.currentTimeMillis()),
					completedAt
				);

				try {
					String body = mapper.writeValueAsString(task);
					int code = api.put("/api/task/update", body, null);
					if (code >= 400) {
						new ErrorDialog("Error", "Failed to update task. Make sure the title is unique.");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					new ErrorDialog("Error", "Failed to update task. Make sure the title is unique.");
					return;
				}

				api.delete("/api/reminder/delete/", taskId);
				api.delete("/api/task-tag/delete/", taskId);
				api.delete("/api/recurring-task/delete/", taskId);
			} else {
				try {
					String body = mapper.writeValueAsString(new TaskDCO(title, description, 1L, selectedPriority, selectedDueDate, projectId));
					int code = api.post("/api/task/create", body);
					if (code >= 400) {
						new ErrorDialog("Error", "Failed to create task. Make sure the title is unique.");
						return;
					}
					// Fetch created task to get its ID
					String projTasksRes = api.get("/api/task/project/", projectId);
					List<Task> projTasks = mapper.readValue(projTasksRes, new TypeReference<List<Task>>() {});
					if (projTasks != null) {
						for (Task t : projTasks) {
							if (title.equals(t.taskTitle())) {
								taskId = t.taskId();
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					new ErrorDialog("Error", "Failed to create task. Make sure the title is unique.");
					return;
				}
			}

			if (selectedReminderTime != null && taskId != null) {
				try {
					Reminder reminder = new Reminder(taskId, selectedReminderTime, selectedReminderMsg);
					String body = mapper.writeValueAsString(reminder);
					api.post("/api/reminder/create", body);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (taskId != null && selectedTags != null) {
				for (Tag tag : selectedTags) {
					try {
						String body = mapper.writeValueAsString(new TaskTag(taskId, tag.tagId()));
						api.post("/api/task-tag/create", body);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			if (taskId != null && isRecurring && selectedRecurringDays != null && !selectedRecurringDays.isEmpty()) {
				try {
					String body = mapper.writeValueAsString(selectedRecurringDays);
					api.post("/api/recurring-task/create/" + taskId, body);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//				Refresh main ui
				main.destroyChildWindows();
				main.refreshWindow();
				
				if(rowPanel instanceof ReminderRowPanel) {
					main.getRemindersButton().doClick();
				}
				else if(rowPanel instanceof ProjectRowPanel) {
					
				}
//				Refresh propject info panel and main window
				ProjectInfoPanel.getProjectInfoPanel().listTasks();
//				Close this ui
				dispose();
		});
	}

	private void setupDueDateMenu(JButton button) {
		JPopupMenu dateMenu = new JPopupMenu();
		JMenuItem todayItem = new JMenuItem("Today");
		JMenuItem tomorrowItem = new JMenuItem("Tomorrow");
		JMenuItem nextWeekItem = new JMenuItem("Next Week");
		JMenuItem customItem = new JMenuItem("Custom Date");
		JMenuItem clearDateItem = new JMenuItem("Clear Date");

		dateMenu.add(todayItem);
		dateMenu.add(tomorrowItem);
		dateMenu.add(nextWeekItem);
		dateMenu.addSeparator();
		dateMenu.add(customItem);
		dateMenu.add(clearDateItem);

		button.addActionListener(_ -> dateMenu.show(button, 0, -button.getHeight()));

		todayItem.addActionListener(_ -> {
			selectedDueDate = LocalDate.now();
			button.setText(selectedDueDate.toString());
			button.setForeground(new Color(42, 157, 143));
		});

		tomorrowItem.addActionListener(_ -> {
			selectedDueDate = LocalDate.now().plusDays(1);
			button.setText(selectedDueDate.toString());
			button.setForeground(new Color(42, 157, 143));
		});

		nextWeekItem.addActionListener(_ -> {
			selectedDueDate = LocalDate.now().plusWeeks(1);
			button.setText(selectedDueDate.toString());
			button.setForeground(new Color(42, 157, 143));
		});

		customItem.addActionListener(_ -> {
			DatePicker picker = new DatePicker();
			picker.setDateToToday();
			
			JDialog inputDialog = new JDialog(CreateUpdateTaskWindow.this, "Due-Date", true);
			
			Container contentPane = inputDialog.getContentPane();
			
			contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
			contentPane.add(picker);
			
			inputDialog.setAlwaysOnTop(true);
			inputDialog.setResizable(false);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new WrapLayout(FlowLayout.RIGHT, 20, 5));
			contentPane.add(buttonPanel);
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(_->inputDialog.dispose());
			buttonPanel.add(cancelButton);
			
			JButton addButton = new JButton("Add");
			addButton.addActionListener(_->{
				selectedDueDate = picker.getDate();
				button.setText(selectedDueDate.toString());
				button.setForeground(new Color(42, 157, 143));
				inputDialog.dispose();
			});
			buttonPanel.add(addButton);
			
			inputDialog.setSize(getPreferredSize());
			inputDialog.setVisible(true);	
		});

		clearDateItem.addActionListener(_ -> {
			selectedDueDate = null;
			button.setText("Due Date");
			button.setForeground(null);
		});
	}

	private void setupPriorityMenu(JButton button) {
		JPopupMenu priorityMenu = new JPopupMenu();
		JMenuItem highItem = new JMenuItem("High");
		JMenuItem mediumItem = new JMenuItem("Medium");
		JMenuItem lowItem = new JMenuItem("Low");
		JMenuItem clearPriorityItem = new JMenuItem("Clear Priority");

		priorityMenu.add(highItem);
		priorityMenu.add(mediumItem);
		priorityMenu.add(lowItem);
		priorityMenu.addSeparator();
		priorityMenu.add(clearPriorityItem);

		button.addActionListener(_ -> priorityMenu.show(button, 0, -button.getHeight()));

		highItem.addActionListener(_ -> {
			selectedPriority = 1;
			button.setText("High");
			button.setForeground(new Color(239, 68, 68));
		});

		mediumItem.addActionListener(_ -> {
			selectedPriority = 2;
			button.setText("Medium");
			button.setForeground(new Color(245, 158, 11));
		});

		lowItem.addActionListener(_ -> {
			selectedPriority = 3;
			button.setText("Low");
			button.setForeground(new Color(16, 185, 129));
		});

		clearPriorityItem.addActionListener(_ -> {
			selectedPriority = null;
			button.setText("Priority");
			button.setForeground(null);
		});
	}

	private void setupReminderMenu(JButton button) {
		JPopupMenu reminderMenu = new JPopupMenu();
		JMenuItem addReminderItem = new JMenuItem("Add/Edit Reminder");
		JMenuItem clearReminderItem = new JMenuItem("Clear Reminder");

		reminderMenu.add(addReminderItem);
		reminderMenu.add(clearReminderItem);

		button.addActionListener(_ -> {
			reminderMenu.show(button, 0, -button.getHeight());
		});

		addReminderItem.addActionListener(_ ->new ReminderDialog(CreateUpdateTaskWindow.this, button));

		clearReminderItem.addActionListener(_ -> {
			selectedReminderTime = null;
			selectedReminderMsg = null;
			button.setText("Reminder");
			button.setForeground(null);

			isRecurring = false;

			selectedRecurringDays.clear();

			selectedDueDate = null;
			dueDateBtn.setText("Due Date");
			dueDateBtn.setForeground(null);
			dueDateBtn.setEnabled(true);

			revalidate();
			repaint();
		});
	}

	private void setupTagsDialog(JButton button) {
		button.addActionListener(_ -> {
			JDialog tagsDialog = new JDialog(CreateUpdateTaskWindow.this);
			tagsDialog.setTitle("Select Tags");
			tagsDialog.setModal(true);
			tagsDialog.setResizable(false);
			tagsDialog.setUndecorated(true);
			tagsDialog.setLayout(new BorderLayout(10, 10));
			
			tagsDialog.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					tagsDialog.dispose();
				}
				@Override
				public void focusGained(FocusEvent arg0) {
					// No need
					}
			});

			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

			List<Tag> allTags = Collections.emptyList();
			try {
				String res = api.get("/api/tag/get-all");
				allTags = mapper.readValue(res, new TypeReference<List<Tag>>() {});
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<JCheckBox> checkBoxes = new ArrayList<>();
			List<Tag> tagsList = new ArrayList<>();

			if (allTags == null || allTags.isEmpty()) {
				mainPanel.add(new JLabel("No tags found."));
			} else {
				for (Tag tag : allTags) {
					JPanel tagRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
					JCheckBox cb = new JCheckBox();
					if (selectedTags.contains(tag)) {
						cb.setSelected(true);
					}
					JLabel label = new JLabel(tag.tagName());
					label.setFont(new Font("Dialog", Font.PLAIN, 14));

					IconColor ic = null;
					try {
						String res = api.get("/api/icon-color/tag/", tag.tagId());
						ic = mapper.readValue(res, IconColor.class);
					} catch (Exception e) {
						e.printStackTrace();
					}

					Color color = (ic == null) ? Color.GRAY : new Color(ic.red(), ic.green(), ic.blue());
					label.setIcon(new CustomIcon(color, 12, 12));

					tagRow.add(cb);
					tagRow.add(label);
					mainPanel.add(tagRow);

					checkBoxes.add(cb);
					tagsList.add(tag);
				}
			}

			JScrollPane scrollPane = new JScrollPane(mainPanel);
			scrollPane.setPreferredSize(new Dimension(250, 200));
			tagsDialog.add(scrollPane, BorderLayout.CENTER);

			JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			buttonPane.setBorder(new EmptyBorder(0, 15, 15, 15));

			JButton cancelButton = new JButton("Cancel");
			cancelButton.putClientProperty("JButton.buttonType", "roundRect");
			cancelButton.addActionListener(_ -> tagsDialog.dispose());

			JButton okButton = new JButton("OK");
			okButton.putClientProperty("JButton.buttonType", "roundRect");
			okButton.addActionListener(_ -> {
				selectedTags.clear();
				for (int i = 0; i < checkBoxes.size(); i++) {
					if (checkBoxes.get(i).isSelected()) {
						selectedTags.add(tagsList.get(i));
					}
				}
				updateTagsButton(button);
				tagsDialog.dispose();
			});

			buttonPane.add(cancelButton);
			buttonPane.add(okButton);
			tagsDialog.add(buttonPane, BorderLayout.SOUTH);

			tagsDialog.pack();
			tagsDialog.setLocationRelativeTo(CreateUpdateTaskWindow.this);
			tagsDialog.setVisible(true);
		});
	}

	private void updateTagsButton(JButton button) {
		if (selectedTags.isEmpty()) {
			button.setText("Tags");
			button.setForeground(null);
		} else if (selectedTags.size() == 1) {
			button.setText(selectedTags.get(0).tagName());
			button.setForeground(new Color(59, 130, 246));
		} else {
			button.setText(selectedTags.size() + " Tags Selected");
			button.setForeground(new Color(59, 130, 246));
		}
	}

}
