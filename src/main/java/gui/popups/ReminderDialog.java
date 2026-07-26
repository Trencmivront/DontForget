package main.java.gui.popups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.zinternaltools.WrapLayout;

import main.java.controllers.RecurringTaskController;
import main.java.controllers.ReminderController;
import main.java.custom.SpringContext;
import main.java.dto.ReminderDTO;
import main.java.gui.Main;
import main.java.gui.windows.CreateUpdateTaskWindow;

public class ReminderDialog extends JDialog {

	private static final Logger logger = LoggerFactory.getLogger(ReminderDialog.class.getName());

	private static final long serialVersionUID = 1L;
	private boolean tempIsRecurring;
	private List<DayOfWeek> tempSelectedRecurringDays;
	private JPanel daysPanel = new JPanel();
	private ButtonGroup radioGroup = new ButtonGroup();
	private JPanel radioPanel;
	
	private CreateUpdateTaskWindow source;
	private JButton reminderBtn;
	private ReminderDTO reminderDTO;
	private boolean isUpdate;
	
	private final RecurringTaskController recurringTaskController;
	private final ReminderController reminderController;
	
	public ReminderDialog(Long reminderId) {
		logger.info("Initializing ReminderDialog");
		super(Main.getMain(), "Reminder", false);
		setAlwaysOnTop(true);
		setResizable(false);
		setUndecorated(true);
		setLayout(new BorderLayout(10, 10));
		
		if(CreateUpdateTaskWindow.getCreateUpdateTaskWindow() != null) {
			this.source = CreateUpdateTaskWindow.getCreateUpdateTaskWindow();
			this.reminderBtn = CreateUpdateTaskWindow.getCreateUpdateTaskWindow().getReminderBtn();
		}
		this.recurringTaskController = SpringContext.getBean(RecurringTaskController.class);
		this.reminderController = SpringContext.getBean(ReminderController.class);
		
		reminderDTO = reminderController.getReminderById(reminderId).getBody();

		DateTimePicker picker = new DateTimePicker();
		
		isUpdate = reminderDTO == null ? false : true;

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		// Custom reminder message input (optional)
		JTextField msgField = new JTextField();
		msgField.putClientProperty("JTextField.placeholderText", "Custom message (optional)");
		msgField.putClientProperty("JTextField.margin", new Insets(4, 6, 4, 6));
		
		if(isUpdate) {
			picker.setDateTimePermissive(reminderDTO.getRemindAt());
			// Temporary variables to track recurring choices inside this dialog
			tempSelectedRecurringDays = recurringTaskController.getRecurringDaysOfTask(reminderId).getBody();
			if(!tempSelectedRecurringDays.isEmpty() || tempSelectedRecurringDays != null) {
				tempIsRecurring = true;
			}
			msgField.setText(reminderDTO.getMessage());
		}else {
			picker.setDateTimePermissive(LocalDateTime.now().plusHours(1));
		}
		
		// Content fields panel containing DatePicker, Custom message, and repeat checkbox
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
		
		fieldsPanel.add(picker);
		fieldsPanel.add(Box.createVerticalStrut(10));
		fieldsPanel.add(msgField);
		fieldsPanel.add(Box.createVerticalStrut(10));

		// Repeat checkbox
		JCheckBox repeatCheckBox = new JCheckBox("repeat");
		repeatCheckBox.setSelected(tempIsRecurring);
		fieldsPanel.add(repeatCheckBox);
		fieldsPanel.add(new JSeparator());
		
//		These radio buttons are for simplicity during day selection
//		If user just want's it to repeat every week, he has option
		JRadioButton onceRadioButton = new JRadioButton("Once A Week");
		JRadioButton everyDayRadioButton = new JRadioButton("Every Day");
		JRadioButton specialRadioButton = new JRadioButton("Select Days");
		
		addOnceRadioButtonActionListener(onceRadioButton);
		addEveryDayRadioButtonActionListener(everyDayRadioButton);
		addSpecialRadioButtonActionListener(specialRadioButton);
		
		radioPanel = new JPanel();
		radioPanel.setLayout(new WrapLayout(FlowLayout.CENTER, 10, 2));
		radioPanel.add(onceRadioButton);
		radioPanel.add(everyDayRadioButton);
		radioPanel.add(specialRadioButton);
		radioGroup.add(onceRadioButton);
		radioGroup.add(everyDayRadioButton);
		radioGroup.add(specialRadioButton);
		radioPanel.setVisible(false);
		fieldsPanel.add(radioPanel);
		fieldsPanel.add(new JSeparator());
		
		addRepeatCheckBoxEventListener(repeatCheckBox);
		
		fieldsPanel.add(daysPanel);
		
		mainPanel.add(fieldsPanel, BorderLayout.CENTER);
		add(mainPanel, BorderLayout.CENTER);

		// Bottom Buttons (Cancel & OK)
		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonPane.setBorder(new EmptyBorder(0, 15, 15, 15));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.putClientProperty("JButton.buttonType", "roundRect");
		addCancelButtonActionListener(cancelButton);
		
		JButton okButton = new JButton("OK");
		okButton.putClientProperty("JButton.buttonType", "roundRect");
		addOkButtonActionListener(okButton, picker, msgField);
		
		buttonPane.add(cancelButton);
		buttonPane.add(okButton);
		add(buttonPane, BorderLayout.SOUTH);
		
		getRootPane().setDefaultButton(okButton);

		addFocusListener();
		pack();
		setLocationRelativeTo(Main.getMain());
		setVisible(true);
	}	
	
	private void setDaysPanel() {
		daysPanel.removeAll();
		daysPanel.setLayout(new BoxLayout(daysPanel, BoxLayout.X_AXIS));
		daysPanel.setVisible(false);
				
		for(int i = 1; i <= 7; i ++) {
			DayOfWeek day = DayOfWeek.of(i);
			String dayString = day.name();
			JCheckBox box = new JCheckBox();
			box.setText(dayString);
			addDayCheckBoxActionListener(box);
			daysPanel.add(box);
			if(tempSelectedRecurringDays.contains(day)) {
				box.setSelected(true);
			}
			box.putClientProperty("name", day);
		}
		
		if(tempIsRecurring) {
			daysPanel.setVisible(true);
		}
	}
	
	private void addDayCheckBoxActionListener(JCheckBox box) {
		box.addActionListener(_->{
			DayOfWeek day = (DayOfWeek)box.getClientProperty("name");
			if(box.isSelected()) {
				tempSelectedRecurringDays.add(day);
			}
			else {
				tempSelectedRecurringDays.remove(day);
			}
		});
	}
	
	private void addRepeatCheckBoxEventListener(JCheckBox repeatCheckBox) {
		repeatCheckBox.addActionListener(_ -> {
			if(repeatCheckBox.isSelected()) {
				radioPanel.setVisible(true);
				tempIsRecurring = true;
			}
			else {
				radioPanel.setVisible(false);
				tempIsRecurring = false;
				tempSelectedRecurringDays.clear();
			}
			pack();

		});
	}
	
	private void addOnceRadioButtonActionListener(JRadioButton rb) {
		rb.addActionListener(_->{
			tempSelectedRecurringDays.clear();
			if(rb.isSelected()) {
				daysPanel.setVisible(false);
				tempSelectedRecurringDays.add(LocalDate.now().getDayOfWeek());
			}
			pack();
		});
	}
	
	private void addEveryDayRadioButtonActionListener(JRadioButton rb) {
		rb.addActionListener(_->{
			tempSelectedRecurringDays.clear();
			if(rb.isSelected()) {
				daysPanel.setVisible(false);
				for(int i = 1; i <= 7; i++) {
					tempSelectedRecurringDays.add(DayOfWeek.of(i));
				}
			}
			pack();
		});
	}
	
	private void addSpecialRadioButtonActionListener(JRadioButton rb) {
		rb.addActionListener(_->{
			tempSelectedRecurringDays.clear();
			if(rb.isSelected()) {
				setDaysPanel();
				daysPanel.setVisible(true);
			}
			pack();
		});
	}

	private void addCancelButtonActionListener(JButton cancelButton) {
		cancelButton.addActionListener(_ -> dispose());
	}

	private void addOkButtonActionListener(JButton okButton, DateTimePicker picker, JTextField msgField) {
		okButton.addActionListener(_ -> {
			LocalDateTime ldt = picker.getDateTimeStrict();
			if (ldt == null) {
				new ErrorDialog("Invalid Date/Time", "Please select a valid date and time.");
				return;
			}
			if (ldt.isBefore(LocalDateTime.now())) {
				new ErrorDialog("Invalid Date/Time", "Reminder time cannot be in the past.");
				return;
			}
			
			if(source != null) {
				source.setSelectedReminderTime(Timestamp.valueOf(ldt));
				String msgText = msgField.getText().trim();
				source.setSelectedReminderMsg(msgText.isEmpty() ? null : msgText);
				// Set recurring state from temp variables
				source.setRecurring(tempIsRecurring);
				source.getSelectedRecurringDays().clear();
				source.getSelectedRecurringDays().addAll(tempSelectedRecurringDays);
				source.setSelectedDueDate(null);
				if (source.isRecurring()) {
					source.getDueDateBtn().setText("Disabled");
					source.getDueDateBtn().setToolTipText("Can't set due date when\nrecurring task is enabled");	
					source.getDueDateBtn().setForeground(null);
					source.getDueDateBtn().setEnabled(false);
				} else {
					source.getDueDateBtn().setText(source.getSelectedDueDate() != null ? source.getSelectedDueDate().toString():"Due Date");
					source.getDueDateBtn().setForeground(new Color(42, 157, 143));
					source.getDueDateBtn().setEnabled(true);
				}
				reminderBtn.setText("Remind: " + ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
				reminderBtn.setForeground(new Color(59, 130, 246));
				source.revalidate();
				source.repaint();
			}
			
//		otherwise, if it is update, just update it
			if(isUpdate) {
				String msgText = msgField.getText().trim();
				ReminderDTO updatedDTO = new ReminderDTO(
					reminderDTO.getTaskId(),
					ldt,
					msgText.isEmpty() ? null : msgText
				);
				reminderController.updateReminder(updatedDTO);
				recurringTaskController.updateRecurringTask(reminderDTO.getTaskId(), tempSelectedRecurringDays);
			}

			dispose();
		});
	}
	
	private void addFocusListener() {
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				dispose();
			}
			@Override
			public void focusGained(FocusEvent arg0) {			
				// I don't use this
			}
		});
	}
}
