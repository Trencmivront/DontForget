package main.io.github.trencmivront.dontforget.gui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.github.lgooddatepicker.components.DatePicker;

import main.io.github.trencmivront.dontforget.controllers.RecurringTaskController;
import main.io.github.trencmivront.dontforget.controllers.ReminderController;
import main.io.github.trencmivront.dontforget.controllers.TagController;
import main.io.github.trencmivront.dontforget.controllers.TaskController;
import main.io.github.trencmivront.dontforget.controllers.TaskTagController;
import main.io.github.trencmivront.dontforget.custom.DocumentFilterFactory;
import main.io.github.trencmivront.dontforget.custom.ScriptWriter;
import main.io.github.trencmivront.dontforget.custom.SpringContext;
import main.io.github.trencmivront.dontforget.dto.ReminderDTO;
import main.io.github.trencmivront.dontforget.dto.TagDTO;
import main.io.github.trencmivront.dontforget.dto.TaskDTO;
import main.io.github.trencmivront.dontforget.dto.TaskTagDTO;
import main.io.github.trencmivront.dontforget.enums.Icon;
import main.io.github.trencmivront.dontforget.enums.Priority;
import main.io.github.trencmivront.dontforget.enums.Script;
import main.io.github.trencmivront.dontforget.gui.Main;
import main.io.github.trencmivront.dontforget.gui.panels.ProjectInfoPanel;
import main.io.github.trencmivront.dontforget.gui.popups.ErrorDialog;
import main.io.github.trencmivront.dontforget.gui.popups.ReminderDialog;
import main.io.github.trencmivront.dontforget.gui.popups.ScriptDialog;
import main.io.github.trencmivront.dontforget.gui.popups.TagsDialog;

public class TaskWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TaskWindow.class.getName());

	private LocalDate selectedDueDate = null;
	private Priority selectedPriority = Priority.NONE;

	private Long projectId;
	private JTextField titleField;
	private JTextArea descArea;
	private List<TagDTO> selectedTags = new ArrayList<>();

	private Timestamp selectedReminderTime = null;
	private String selectedReminderMsg = null;
	private boolean isRecurring = false;
	private List<DayOfWeek> selectedRecurringDays = new ArrayList<>();

	private JButton dueDateBtn;
	private JButton tagsBtn;
	private JButton priorityBtn;
	private JButton reminderBtn;
	private JButton scriptBtn;
	private static final Main main = Main.getMain();

	private boolean runsScript = false;
	private String osTypeExtension = Script.getCurrentOsScript().getExtension();
	private boolean isUpdate;
	private TaskDTO updateTaskDTO;

	private final TaskController taskController = SpringContext.getBean(TaskController.class);
	private final TagController tagController = SpringContext.getBean(TagController.class);
	private final RecurringTaskController recurringTaskController = SpringContext.getBean(RecurringTaskController.class);
	private final ReminderController reminderController = SpringContext.getBean(ReminderController.class);
	private final TaskTagController taskTagController = SpringContext.getBean(TaskTagController.class);
	
	// Icons
	private final ImageIcon redFlagIcon = Icon.RED_FALG.getSmallIcon(),
			greenFlagIcon = Icon.GREEN_FLAG.getSmallIcon(),
			yellowFlagIcon = Icon.YELLOW_FLAG.getSmallIcon(),
			noneFlagIcon = Icon.NONE_FLAG.getSmallIcon(),
			tagIcon = Icon.TAG.getSmallIcon();
	
	private static final int TITLE_MAX_LENGTH = 100;
	private static final int BODY_MAX_LENGTH = 1000;
	
	private static TaskWindow taskWindow;
	
	public LocalDate getSelectedDueDate() {
		return selectedDueDate;
	}

	public void setSelectedDueDate(LocalDate selectedDueDate) {
		this.selectedDueDate = selectedDueDate;
	}

	public Priority getSelectedPriority() {
		return selectedPriority;
	}

	public void setSelectedPriority(Priority selectedPriority) {
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

	public List<TagDTO> getSelectedTags() {
		return selectedTags;
	}

	public void setSelectedTags(List<TagDTO> selectedTags) {
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

	public JButton getReminderBtn() {
		return reminderBtn;
	}
	
	public static TaskWindow getTaskWindow() {
		return taskWindow;
	}
	
//	if it is update, taskId is not null and projectId is null
//	if it is new task, taskId is null and projectId is not null
	public TaskWindow(Long taskId, Long projectId) {
//		only one instance of task window at a time
		if(taskWindow != null) {
			taskWindow.dispose();
			taskWindow = null;
		}
		
		super(main, "Task");
		logger.info("Initializing TaskWindow.");
		taskWindow = this;

		if(taskId != null) {
			isUpdate = true;
			updateTaskDTO = taskController.getTaskById(taskId).getBody();
			this.projectId = updateTaskDTO.getProjectId();
		} else if (projectId != null){
			isUpdate = false;
			this.projectId = projectId;
		} else {
			taskId = 1l;
			projectId = 1l;
			logger.error("GO TO HELL");
		}
		
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Window activeWindow = Main.getMain();

		if (activeWindow != null) {
			Dimension size = activeWindow.getSize();
			int w = Math.min(480, (int) (size.getWidth() * 0.75));
			setMinimumSize(new Dimension(w, 0));
		} else {
			setMinimumSize(new Dimension(480, 0));
		}

		// Content Panel with standard margin
		JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// 1. Task Title (Header Panel)
		titleField = new JTextField();
		titleField.setFont(UIManager.getFont("Button.font"));
		titleField.putClientProperty("JTextField.placeholderText", "Title of the task");
		titleField.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		setTitleFieldDocumentFilter(titleField);
		contentPanel.add(titleField, BorderLayout.NORTH);

		// 2. Center Panel (Description + Options)
		JPanel centerPanel = new JPanel(new BorderLayout(0, 12));

		descArea = new JTextArea();
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		descArea.putClientProperty("JTextArea.placeholderText", "Add details or description...");
		descArea.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		addTextAreaCaretListener(descArea);
		setDescriptionFieldDocumentFilter(descArea);
		
		JScrollPane descScrollPane = new JScrollPane(descArea);
		centerPanel.add(descScrollPane, BorderLayout.CENTER);

		// Options bar under description
		JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		optionsPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

		dueDateBtn = new JButton(Icon.CALENDAR.getSmallIcon());
		dueDateBtn.putClientProperty("JButton.buttonType", "roundRect");

		priorityBtn = new JButton(noneFlagIcon);
		priorityBtn.putClientProperty("JButton.buttonType", "roundRect");

		reminderBtn = new JButton(Icon.ADD_REMINDER.getSmallIcon());
		reminderBtn.putClientProperty("JButton.buttonType", "roundRect");

		tagsBtn = new JButton(tagIcon);
		tagsBtn.putClientProperty("JButton.buttonType", "roundRect");

		scriptBtn = new JButton(Icon.ADD_FILE.getSmallIcon());
		scriptBtn.putClientProperty("JButton.buttonType", "roundRect");

		optionsPanel.add(dueDateBtn);
		optionsPanel.add(priorityBtn);
		optionsPanel.add(reminderBtn);
		optionsPanel.add(tagsBtn);
		optionsPanel.add(scriptBtn);

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
		
		addCreateButtonActionListener(createButton, isUpdate);

		if (isUpdate && updateTaskDTO != null) {
			String taskTitle = updateTaskDTO.getTaskTitle();
			if (taskTitle != null) {
				titleField.setText(taskTitle);
			}
			String description = updateTaskDTO.getDescription();
			if (description != null) {
				descArea.setText(description);
			}
			
			LocalDate dueDate = updateTaskDTO.getDueDate();
			Priority priority = Priority.getPriority(updateTaskDTO.getPriority());
			
			setDueDate(dueDate);
			setPriority(priority);
			if(taskId != null) {
				setReminder(taskId);
				setRecurringDays(taskId);
				setTag(taskId);
				setScript(Script.getTaskScript(taskId) != null ? true:false);
			}
		}
		
		// Set up dropdown popups for the option buttons
		setupDueDateMenu();
		setupPriorityMenu();
		setupReminderMenu();
		setupTagsDialog();
		setupScriptButton();
		
		addFocusListener();
		addWindowCloseListener();
		refresh();
		setLocationRelativeTo(main);
		setVisible(true);
		logger.info("TaskWindow display complete.");
	}
	
	private void setDueDate(LocalDate dueDate) {
		if (dueDate != null) {
			selectedDueDate = dueDate;
			dueDateBtn.setText(selectedDueDate.toString());
			dueDateBtn.setForeground(new Color(42, 157, 143));
		}		
	}

	private void setTag(Long taskId) {
		List<TagDTO> tags = Collections.emptyList();
		try {
			ResponseEntity<List<TagDTO>> response = tagController.getTagsOfTask(taskId);
			tags = response.getBody();
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
			ResponseEntity<List<DayOfWeek>> response = recurringTaskController.getRecurringDaysOfTask(taskId);
			recurringDays = response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (recurringDays != null && !recurringDays.isEmpty()) {
			selectedRecurringDays.addAll(recurringDays);
			isRecurring = true;
			dueDateBtn.setIcon(Icon.PROHIBITION.getSmallIcon());
			dueDateBtn.setText(null);
			dueDateBtn.setToolTipText("Can't set due date when\nrecurring task is enabled");
			dueDateBtn.setForeground(null);
			dueDateBtn.setEnabled(false);
		}
	}

	private void setReminder(Long taskId) {
		ReminderDTO reminder = null;
		try {
			ResponseEntity<ReminderDTO> response = reminderController.getReminderById(taskId);
			reminder = response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (reminder != null) {
			selectedReminderTime = reminder.getRemindAt() != null ? Timestamp.valueOf(reminder.getRemindAt()) : null;
			selectedReminderMsg = reminder.getMessage();
			if (selectedReminderTime != null) {
				LocalDateTime ldt = selectedReminderTime.toLocalDateTime();
				reminderBtn.setIcon(Icon.RINGING.getSmallIcon());
				reminderBtn.setText(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
				reminderBtn.setForeground(new Color(59, 130, 246));
			}
		}
	}

	private void setPriority(Priority priority) {
		if (priority != null) {
			selectedPriority = priority;
			if (priority == Priority.HIGH) {
				priorityBtn.setIcon(redFlagIcon);
			} else if (priority == Priority.MEDIUM) {
				priorityBtn.setIcon(yellowFlagIcon);
			} else if (priority == Priority.LOW) {
				priorityBtn.setIcon(greenFlagIcon);
			}
		}
	}

	private void addCreateButtonActionListener(JButton button, boolean isUpdate) {
		button.addActionListener(_->{
			String title = titleField.getText().trim();
			String description = descArea.getText().trim();

			if (title.isEmpty()) {
				new ErrorDialog("Title Empty", "Task title cannot be empty.");
				return;
			}

			Long taskId = null;
			if (isUpdate) {
				taskId = updateTaskDTO.getTaskId();
				Long statusId = updateTaskDTO.getStatusId();
				if (statusId == null) statusId = 1L;

				TaskDTO task = new TaskDTO(
					taskId,
					title,
					description,
					statusId,
					selectedPriority.getValue(),
					selectedDueDate,
					projectId,
					runsScript
				);

				try {
					ResponseEntity<String> response = taskController.updateTask(task);
					if (response.getStatusCode().value() >= 400) {
						new ErrorDialog("Error", "Failed to update task. Make sure the title is unique.");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					new ErrorDialog("Error", "Failed to update task. Make sure the title is unique.");
					return;
				}
			} else {
				try {
					ResponseEntity<Long> response = taskController.createTask(new TaskDTO(null, title, description, 1L, selectedPriority.getValue(), selectedDueDate, projectId, runsScript));
					if (response.getStatusCode().value() >= 400) {
						new ErrorDialog("Error", "Failed to create task. Make sure the title is unique.");
						return;
					}
					taskId = response.getBody();
				} catch (Exception e){
					e.printStackTrace();
					new ErrorDialog("Error", "Failed to create task. Make sure the title is unique.");
					return;
				}
			}
			
			if(taskId != null) {
				if (selectedReminderTime != null) {
					try {
						ReminderDTO reminder = new ReminderDTO(taskId, selectedReminderTime.toLocalDateTime(), selectedReminderMsg);
						reminderController.createReminder(reminder);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						reminderController.deleteReminder(taskId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (selectedTags != null || !selectedTags.isEmpty()) {
					taskTagController.deleteTagsOfTask(taskId);
					for (TagDTO tag : selectedTags) {
						try {
							taskTagController.createTaskTag(new TaskTagDTO(taskId, tag.getTagId()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else {
					try {
						taskTagController.deleteTagsOfTask(taskId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (isRecurring && selectedRecurringDays != null && !selectedRecurringDays.isEmpty()) {
					try {
						recurringTaskController.createRecurringTask(taskId, selectedRecurringDays);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(runsScript) {
					Path script =  Script.getNullScript();
					if(script != null) {
						File file = script.toFile();
						File rename = ScriptWriter.getScriptsDir().resolve(taskId + "_script" + osTypeExtension).toFile();
						file.renameTo(rename);
						logger.info("null_script file renamed to {}_script.", taskId);
					}
				}
				else {
					Script.deleteTaskScriptFile(taskId);
				}
			}

			
//			Destroy dialogs
			main.destroyChildWindows();

//			null case
			if(ProjectInfoPanel.getProjectInfoPanel() != null){
//				relist tasks
				ProjectInfoPanel.getProjectInfoPanel().listTasks();
			}
			if(SearchWindow.getSearchWindow() != null) {
				SearchWindow.getSearchWindow().clear();
			}
			
//			refresh window
			main.refreshWindow();
//				Close this ui
			dispose();
		});
	}

	private void setupDueDateMenu() {
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

		dueDateBtn.addActionListener(_ -> dateMenu.show(dueDateBtn, 0, -dueDateBtn.getHeight()));

		todayItem.addActionListener(_ -> {
			selectedDueDate = LocalDate.now();
			dueDateBtn.setText(selectedDueDate.toString());
			dueDateBtn.setForeground(new Color(42, 157, 143));
			refresh();
		});

		tomorrowItem.addActionListener(_ -> {
			selectedDueDate = LocalDate.now().plusDays(1);
			dueDateBtn.setText(selectedDueDate.toString());
			dueDateBtn.setForeground(new Color(42, 157, 143));
			refresh();
		});

		nextWeekItem.addActionListener(_ -> {
			selectedDueDate = LocalDate.now().plusWeeks(1);
			dueDateBtn.setText(selectedDueDate.toString());
			dueDateBtn.setForeground(new Color(42, 157, 143));
			refresh();
		});

		customItem.addActionListener(_ -> {
			DatePicker picker = new DatePicker() {
				@Override
				public boolean isDateAllowed(LocalDate date) {
					if(date.isBefore(LocalDate.now())) {
						return false;
					}
					return true;
				}
			};
			JDialog inputDialog = new JDialog(TaskWindow.this, "Due Date", true);
			
			Container contentPane = inputDialog.getContentPane();
			
			contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
			contentPane.add(picker);
			
			inputDialog.setAlwaysOnTop(true);
			inputDialog.setResizable(false);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 5));
			contentPane.add(buttonPanel);
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(_->inputDialog.dispose());
			buttonPanel.add(cancelButton);
			
			JButton addButton = new JButton("Add");
			addButton.addActionListener(_->{
				if(picker.getDate().isBefore(LocalDate.now())) {
					JOptionPane.showMessageDialog(inputDialog, "Past Date", "The date picked is in past.", JOptionPane.ERROR_MESSAGE);
					dueDateBtn.setText(null);
					dueDateBtn.setForeground(null);
					selectedDueDate = null;
					return;
				}
				selectedDueDate = picker.getDate();
				
				dueDateBtn.setText(selectedDueDate.toString());
				dueDateBtn.setForeground(new Color(42, 157, 143));
				inputDialog.dispose();
			});
			buttonPanel.add(addButton);
			
			inputDialog.pack();
			inputDialog.setVisible(true);	
			refresh();
		});

		clearDateItem.addActionListener(_ -> {
			selectedDueDate = null;
			dueDateBtn.setIcon(Icon.CALENDAR.getSmallIcon());
			dueDateBtn.setText(null);
			dueDateBtn.setForeground(null);
			refresh();
		});
	}

	private void setupPriorityMenu() {
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

		priorityBtn.addActionListener(_ -> priorityMenu.show(priorityBtn, 0, -priorityBtn.getHeight()));

		highItem.addActionListener(_ -> {
			selectedPriority = Priority.HIGH;
			priorityBtn.setIcon(redFlagIcon);
		});

		mediumItem.addActionListener(_ -> {
			selectedPriority = Priority.MEDIUM;
			priorityBtn.setIcon(yellowFlagIcon);
		});

		lowItem.addActionListener(_ -> {
			selectedPriority = Priority.LOW;
			priorityBtn.setIcon(greenFlagIcon);
		});

		clearPriorityItem.addActionListener(_ -> {
			selectedPriority = null;
			priorityBtn.setIcon(noneFlagIcon);
		});
	}

	private void setupReminderMenu() {
		JPopupMenu reminderMenu = new JPopupMenu();
		JMenuItem addReminderItem = new JMenuItem("Add/Edit Reminder");
		JMenuItem clearReminderItem = new JMenuItem("Clear Reminder");

		reminderMenu.add(addReminderItem);
		reminderMenu.add(clearReminderItem);

		reminderBtn.addActionListener(_ -> {
			reminderMenu.show(reminderBtn, 0, -reminderBtn.getHeight());
		});

		addReminderItem.addActionListener(_ ->new ReminderDialog(isUpdate ? updateTaskDTO.getTaskId() : null));

		clearReminderItem.addActionListener(_ -> {
			selectedReminderTime = null;
			selectedReminderMsg = null;
			reminderBtn.setIcon(Icon.ADD_REMINDER.getSmallIcon());
			reminderBtn.setText(null);
			reminderBtn.setForeground(null);

			isRecurring = false;
			
			dueDateBtn.setIcon(Icon.CALENDAR.getSmallIcon());
			if(!selectedRecurringDays.isEmpty()) {
				selectedDueDate = null;
				dueDateBtn.setText(null);
			} else {
				dueDateBtn.setText(selectedDueDate != null ? selectedDueDate.toString():null);
			}

			selectedRecurringDays.clear();
			
			dueDateBtn.setEnabled(true);
			setScript(false);
			refresh();
		});
	}
	
	private void setupTagsDialog() {
		tagsBtn.addActionListener(_ -> {
			TagsDialog tagsDialog = new TagsDialog(new ArrayList<>(selectedTags));

			JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			buttonPane.setBorder(new EmptyBorder(0, 15, 15, 15));

			JButton okButton = new JButton("OK");
			okButton.putClientProperty("JButton.buttonType", "roundRect");
			okButton.addActionListener(_ -> {
				selectedTags = new ArrayList<>(tagsDialog.getSelectedTags());
				updateTagsButton(tagsBtn);
				tagsDialog.dispose();
				refresh();
			});

			buttonPane.add(okButton);
			tagsDialog.add(buttonPane, BorderLayout.SOUTH);

			tagsDialog.pack();
			setLocationToCenter(tagsDialog);
			tagsDialog.setVisible(true);
		});
	}

	private void setupScriptButton() {
		updateScriptButton();
		scriptBtn.addActionListener(_ -> {
			Long currentTaskId = isUpdate ? updateTaskDTO.getTaskId() : null;
			new ScriptDialog(currentTaskId, scriptSet -> {
				runsScript = scriptSet;
				updateScriptButton();
			});
		});
	}

	private void setScript(boolean hasScript) {
		runsScript = hasScript;
		updateScriptButton();
	}

	public void updateScriptButton() {
		if (runsScript && selectedReminderTime != null) {
			scriptBtn.setIcon(Icon.FILE.getSmallIcon());
			scriptBtn.setEnabled(true);
		} else if(selectedReminderTime == null) {
			scriptBtn.setIcon(Icon.PROHIBITION.getSmallIcon());
			scriptBtn.setEnabled(false);
		} else {
			scriptBtn.setIcon(Icon.ADD_FILE.getSmallIcon());
			scriptBtn.setEnabled(true);
		}
	}

	private void updateTagsButton(JButton button) {
		if (selectedTags.isEmpty()) {
			button.setIcon(tagIcon);
			button.setText(null);
			button.setForeground(null);
		} else if (selectedTags.size() == 1) {
			button.setText(selectedTags.get(0).getTagName());
			button.setIcon(tagIcon);
			button.setForeground(new Color(59, 130, 246));
		} else {
			button.setText(Integer.toString(selectedTags.size()));
			button.setIcon(tagIcon);
			button.setForeground(new Color(59, 130, 246));
		}
	}
	
	private void setTitleFieldDocumentFilter(JTextField field) {
		((AbstractDocument)field.getDocument()).setDocumentFilter(DocumentFilterFactory.getDocumentFilter(TITLE_MAX_LENGTH));
	}
	
	private void setDescriptionFieldDocumentFilter(JTextArea field) {
		((AbstractDocument)field.getDocument()).setDocumentFilter(DocumentFilterFactory.getDocumentFilter(BODY_MAX_LENGTH));
	}
	
	private void setLocationToCenter(Window window) {
		int x = (int) main.getLocationOnScreen().getX() + (main.getWidth() / 2 - WIDTH / 2);
		int y = (int) main.getLocationOnScreen().getY() + (main.getHeight() / 2 - HEIGHT / 2);
		window.setLocation(x, y);
	}
	
	private void addTextAreaCaretListener(JTextArea area) {
		area.addCaretListener(e -> {
			try {
		        int caretPos = e.getDot();
		        int line = area.getLineOfOffset(caretPos);
		        int lineStart = area.getLineStartOffset(line);
		        int lineEnd = area.getLineEndOffset(line);
		        
		        if ((caretPos == lineStart || caretPos == lineEnd) && (caretPos == area.getText().length())) {
		        	refresh();
		        }
		        
		    } catch (BadLocationException _) {
		        logger.warn("Bad Caret Location");
		    }
			
		});
	}
	

	
	private void addWindowCloseListener() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Script.deleteNullScriptFile();
			}
		});
	}
	
	private void addFocusListener() {
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				main.destroyChildWindowsExcluding(taskWindow);
				super.windowGainedFocus(e);
			}
		});
	}
	private void refresh() {
		pack();
		revalidate();
		repaint();
	}
}