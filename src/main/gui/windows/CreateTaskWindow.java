package main.gui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.github.lgooddatepicker.components.DateTimePicker;

import main.dco.TaskDCO;
import main.entities.Reminder;
import main.gui.Main;
import main.services.reminder.CreateReminderService;
import main.services.task.CreateTaskService;

public class CreateTaskWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CreateTaskWindow.class.getName());

	private LocalDate selectedDueDate = null;
	private Integer selectedPriority = null;
	private Timestamp selectedReminderTime = null;
	private String selectedReminderMsg = null;
	private JPanel projectPanel;
	private JTextField titleField;
	private JTextArea descArea;

	public CreateTaskWindow(JPanel panel) {
		logger.info("Initializing CreateTaskWindow.");
		
		projectPanel = panel;
				
		setTitle("Create Task");
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
		JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		optionsPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

		JButton dueDateBtn = new JButton("📅 Set Due Date");
		dueDateBtn.putClientProperty("JButton.buttonType", "roundRect");

		JButton priorityBtn = new JButton("🏳️ Set Priority");
		priorityBtn.putClientProperty("JButton.buttonType", "roundRect");

		JButton reminderBtn = new JButton("⏰ Set Reminder");
		reminderBtn.putClientProperty("JButton.buttonType", "roundRect");

		optionsPanel.add(dueDateBtn);
		optionsPanel.add(priorityBtn);
		optionsPanel.add(reminderBtn);
		centerPanel.add(optionsPanel, BorderLayout.SOUTH);

		contentPanel.add(centerPanel, BorderLayout.CENTER);

		// 3. Footer Panel (Cancel + Create buttons)
		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Dialog", Font.PLAIN, 14));
		cancelButton.putClientProperty("JButton.buttonType", "roundRect");
		cancelButton.addActionListener(_ -> dispose());

		JButton createButton = new JButton("CREATE");
		createButton.setFont(new Font("Dialog", Font.BOLD, 14));
		createButton.putClientProperty("JButton.buttonType", "roundRect");

		buttonPane.add(cancelButton);
		buttonPane.add(createButton);
		footerPanel.add(buttonPane, BorderLayout.EAST);
		contentPanel.add(footerPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(createButton);
		
		addCreateButtonActionListener(createButton);

		// Set up dropdown popups for the option buttons
		setupDueDateMenu(dueDateBtn);
		setupPriorityMenu(priorityBtn);
		setupReminderMenu(reminderBtn);

		revalidate();
		repaint();
		setVisible(true);
		logger.info("CreateTaskWindow display complete.");
	}
	
	private void addCreateButtonActionListener(JButton button) {
		button.addActionListener(_->{
			String title = titleField.getText().trim();
			String description = descArea.getText().trim();

			if (title.isEmpty()) {
				JOptionPane.showMessageDialog(CreateTaskWindow.this, "Task title cannot be empty.", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			Integer projectId = (int)projectPanel.getClientProperty("project_id");

			Long taskId = (long)CreateTaskService.execute(new TaskDCO(title, description, 1, selectedPriority, selectedDueDate, projectId));
			
			if(taskId == 0) {
				JOptionPane.showMessageDialog(CreateTaskWindow.this, "Failed to create task. Make sure the title is unique.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			
			if (selectedReminderTime != null && taskId != null) {
				Reminder reminder = new Reminder(taskId, selectedReminderTime, selectedReminderMsg);
				CreateReminderService.execute(reminder);
			}
//				Refresh main ui
				projectPanel.dispatchEvent(new MouseEvent(projectPanel, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, projectPanel.getWidth()/2, projectPanel.getHeight()/2, 1, false));
				Main.refreshWindow();
//				Close this ui
				dispose();
		});
	}

	private void setupDueDateMenu(JButton button) {
		JPopupMenu dateMenu = new JPopupMenu();
		JMenuItem todayItem = new JMenuItem("Today");
		JMenuItem tomorrowItem = new JMenuItem("Tomorrow");
		JMenuItem nextWeekItem = new JMenuItem("Next Week");
		JMenuItem customItem = new JMenuItem("Custom Date (YYYY-MM-DD)...");
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
			button.setText("📅 " + selectedDueDate);
			button.setForeground(new Color(42, 157, 143));
		});

		tomorrowItem.addActionListener(_ -> {
			selectedDueDate = LocalDate.now().plusDays(1);
			button.setText("📅 " + selectedDueDate);
			button.setForeground(new Color(42, 157, 143));
		});

		nextWeekItem.addActionListener(_ -> {
			selectedDueDate = LocalDate.now().plusWeeks(1);
			button.setText("📅 " + selectedDueDate);
			button.setForeground(new Color(42, 157, 143));
		});

		customItem.addActionListener(_ -> {
			
		});

		clearDateItem.addActionListener(_ -> {
			selectedDueDate = null;
			button.setText("📅 Set Due Date");
			button.setForeground(null);
		});
	}

	private void setupPriorityMenu(JButton button) {
		JPopupMenu priorityMenu = new JPopupMenu();
		JMenuItem highItem = new JMenuItem("🔴 High");
		JMenuItem mediumItem = new JMenuItem("🟠 Medium");
		JMenuItem lowItem = new JMenuItem("🟢 Low");
		JMenuItem clearPriorityItem = new JMenuItem("⚪ Clear Priority");

		priorityMenu.add(highItem);
		priorityMenu.add(mediumItem);
		priorityMenu.add(lowItem);
		priorityMenu.addSeparator();
		priorityMenu.add(clearPriorityItem);

		button.addActionListener(_ -> priorityMenu.show(button, 0, -button.getHeight()));

		highItem.addActionListener(_ -> {
			selectedPriority = 1;
			button.setText("🔴 High");
			button.setForeground(new Color(239, 68, 68));
		});

		mediumItem.addActionListener(_ -> {
			selectedPriority = 2;
			button.setText("🟠 Medium");
			button.setForeground(new Color(245, 158, 11));
		});

		lowItem.addActionListener(_ -> {
			selectedPriority = 3;
			button.setText("🟢 Low");
			button.setForeground(new Color(16, 185, 129));
		});

		clearPriorityItem.addActionListener(_ -> {
			selectedPriority = null;
			button.setText("🏳️ Set Priority");
			button.setForeground(null);
		});
	}

	private void setupReminderMenu(JButton button) {
		JPopupMenu reminderMenu = new JPopupMenu();
		JMenuItem addReminderItem = new JMenuItem("⏰ Add/Edit Reminder...");
		JMenuItem clearReminderItem = new JMenuItem("❌ Clear Reminder");

		reminderMenu.add(addReminderItem);
		reminderMenu.add(clearReminderItem);

		button.addActionListener(_ -> reminderMenu.show(button, 0, -button.getHeight()));

		addReminderItem.addActionListener(_ -> {
			DateTimePicker picker = new DateTimePicker();
			picker.setDateTimePermissive(LocalDateTime.now().plusHours(2));
			
			JDialog inputDialog = new JDialog(CreateTaskWindow.this);

			inputDialog.setTitle("Set Reminder");
			inputDialog.setModal(true);
			inputDialog.setAlwaysOnTop(true);
			inputDialog.setResizable(false);
			
			inputDialog.setLayout(new BorderLayout(10, 10));
			
			JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
			mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
			
			// Date Time Picker
			mainPanel.add(picker, BorderLayout.CENTER);
			
			// Custom reminder message input (optional)
			JTextField msgField = new JTextField();
			msgField.putClientProperty("JTextField.placeholderText", "Custom message (optional)");
			msgField.putClientProperty("JTextField.margin", new Insets(4, 6, 4, 6));
			if (selectedReminderMsg != null) {
				msgField.setText(selectedReminderMsg);
			}
			mainPanel.add(msgField, BorderLayout.SOUTH);
			
			inputDialog.add(mainPanel, BorderLayout.CENTER);
			
			// Bottom Buttons (Cancel & OK)
			JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			buttonPane.setBorder(new EmptyBorder(0, 15, 15, 15));
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.putClientProperty("JButton.buttonType", "roundRect");
			cancelButton.addActionListener(_ -> inputDialog.dispose());
			
			JButton okButton = new JButton("OK");
			okButton.putClientProperty("JButton.buttonType", "roundRect");
			
			buttonPane.add(cancelButton);
			buttonPane.add(okButton);
			inputDialog.add(buttonPane, BorderLayout.SOUTH);
			
			inputDialog.getRootPane().setDefaultButton(okButton);
			
			okButton.addActionListener(_ -> {
				LocalDateTime ldt = picker.getDateTimeStrict();
				if (ldt == null) {
					JOptionPane.showMessageDialog(inputDialog, "Please select a valid date and time.",
							"Invalid Date/Time", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (ldt.isBefore(LocalDateTime.now())) {
					JOptionPane.showMessageDialog(inputDialog, "Reminder time cannot be in the past.",
							"Invalid Date/Time", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				selectedReminderTime = Timestamp.valueOf(ldt);
				String msgText = msgField.getText().trim();
				selectedReminderMsg = msgText.isEmpty() ? null : msgText;
				
				button.setText("⏰ Remind: " + ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
				button.setForeground(new Color(59, 130, 246));
				inputDialog.dispose();
			});
			
			if (selectedReminderTime != null) {
				picker.setDateTimePermissive(selectedReminderTime.toLocalDateTime());
			}
			
			inputDialog.pack();
			inputDialog.setLocationRelativeTo(CreateTaskWindow.this);
			inputDialog.setVisible(true);
		});

		clearReminderItem.addActionListener(_ -> {
			selectedReminderTime = null;
			selectedReminderMsg = null;
			button.setText("⏰ Set Reminder");
			button.setForeground(null);
		});
	}

}
