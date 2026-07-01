package main.gui.popup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import main.gui.windows.CreateTaskWindow;

public class ReminderDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private boolean tempIsRecurring;
	private List<DayOfWeek> tempSelectedRecurringDays;
	private JPanel daysPanel = new JPanel();
	private ButtonGroup radioGroup = new ButtonGroup();
	private JPanel radioPanel;
	
	public ReminderDialog(CreateTaskWindow source, JButton reminderBtn) {
		super(source, "Set Reminder", true);
		setAlwaysOnTop(true);
		setResizable(false);
		setLayout(new BorderLayout(10, 10));

		DateTimePicker picker = new DateTimePicker();
		picker.setDateTimePermissive(LocalDateTime.now().plusHours(1));
		if (source.selectedReminderTime != null) {
			picker.setDateTimePermissive(source.selectedReminderTime.toLocalDateTime());
		}

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		// Temporary variables to track recurring choices inside this dialog
		tempIsRecurring = source.isRecurring;
		tempSelectedRecurringDays = new ArrayList<>(source.selectedRecurringDays);
		
		// Content fields panel containing DatePicker, Custom message, and repeat checkbox
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
		fieldsPanel.add(picker);
		
		fieldsPanel.add(Box.createVerticalStrut(10));
		
		// Custom reminder message input (optional)
		JTextField msgField = new JTextField();
		msgField.putClientProperty("JTextField.placeholderText", "Custom message (optional)");
		msgField.putClientProperty("JTextField.margin", new Insets(4, 6, 4, 6));
		if (source.selectedReminderMsg != null) {
			msgField.setText(source.selectedReminderMsg);
		}
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
		addOkButtonActionListener(okButton, picker, msgField, source, reminderBtn);
		
		buttonPane.add(cancelButton);
		buttonPane.add(okButton);
		add(buttonPane, BorderLayout.SOUTH);
		
		getRootPane().setDefaultButton(okButton);

		pack();
		setLocationRelativeTo(source);
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

	private void addOkButtonActionListener(JButton okButton, DateTimePicker picker, JTextField msgField, CreateTaskWindow source, JButton reminderBtn) {
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
			
			source.selectedReminderTime = Timestamp.valueOf(ldt);
			String msgText = msgField.getText().trim();
			source.selectedReminderMsg = msgText.isEmpty() ? null : msgText;
			
			reminderBtn.setText("Remind: " + ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
			reminderBtn.setForeground(new Color(59, 130, 246));

			// Commit recurring state from temp variables
			source.isRecurring = tempIsRecurring;
			source.selectedRecurringDays.clear();
			source.selectedRecurringDays.addAll(tempSelectedRecurringDays);
			source.selectedDueDate = null;

			if (source.isRecurring) {
				source.dueDateBtn.setText("Disabled");
				source.dueDateBtn.setToolTipText("Can't set due date when\nrecurring task is enabled");	
				source.dueDateBtn.setForeground(null);
				source.dueDateBtn.setEnabled(false);
			} else {
				source.dueDateBtn.setText(source.selectedDueDate != null ? source.selectedDueDate.toString():null);
				source.dueDateBtn.setForeground(new Color(42, 157, 143));
				source.dueDateBtn.setEnabled(true);
			}
			source.revalidate();
			source.repaint();

			dispose();
		});
	}
}
