package main.gui.panels;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.lgooddatepicker.zinternaltools.WrapLayout;

import main.gui.Main;
import main.services.reminder.DeleteReminderService;

public class ReminderActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	final JButton editButton = new JButton("Edit");
	final JButton deleteButton = new JButton("Delete");
	
	private Long reminderId;

	public ReminderActionPanel() {
		this(null);
	}

	public ReminderActionPanel(Long reminderId) {
		this.reminderId = reminderId;
		setLayout(new WrapLayout(WrapLayout.CENTER, 5, 1));
		add(editButton);
		add(deleteButton);
		
		addEditButtonListener();
		addDeleteButtonListener();
	}

	public void setReminderId(Long reminderId) {
		this.reminderId = reminderId;
	}

	public Long getReminderId() {
		return reminderId;
	}

	private void addEditButtonListener() {
		editButton.addActionListener(_ -> {
			// Edit part skipped for now
		});
	}

	private void addDeleteButtonListener() {
		deleteButton.addActionListener(_ -> {
			if (reminderId == null) {
				return;
			}
			int confirm = JOptionPane.showConfirmDialog(
				this,
				"Are you sure you want to delete this reminder?",
				"Confirm Delete",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
			);
			if (confirm == JOptionPane.YES_OPTION) {
				if (DeleteReminderService.execute(reminderId)) {
					refreshRemindersList();
				} else {
					JOptionPane.showMessageDialog(this, "Failed to delete the reminder.");
				}
			}
		});
	}

	private void refreshRemindersList() {
		Main.getMain().getRemindersButton().doClick();
	}
}
