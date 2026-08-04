package main.java.gui.panels;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import main.java.controllers.ReminderController;
import main.java.controllers.TaskController;
import main.java.custom.SpringContext;
import main.java.dto.ReminderDTO;
import main.java.dto.TaskDTO;
import main.java.gui.panels.rows.ReminderRowPanel;

public class ReminderPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTabbedPane monthTabbedPane;
	private ReminderController reminderController;
	private TaskController taskController;

	private static final Logger logger = LoggerFactory.getLogger(ReminderPanel.class.getName());

	public ReminderPanel() {
		logger.info("Drawing the Reminder panel.");
		this.reminderController = SpringContext.getBean(ReminderController.class);
		this.taskController = SpringContext.getBean(TaskController.class);

		setLayout(new BorderLayout());

		monthTabbedPane = new JTabbedPane(SwingConstants.LEFT);
		monthTabbedPane.setSelectedIndex(-1);

		addTabs();

		add(new HeaderPanel("Reminders"), BorderLayout.NORTH);
		add(monthTabbedPane, BorderLayout.CENTER);
		logger.info("Window is ready.");
	}

	private void addTabs() {
		Map<Integer, Map<String, List<ReminderDTO>>> grouped = getGroupedReminders();

		if (grouped.isEmpty()) {
			monthTabbedPane.addTab("Empty", new EmptyPanel("You don't have any reminder."));
			return;
		}

		createTabs(grouped);
	}

	private Map<Integer, Map<String, List<ReminderDTO>>> getGroupedReminders() {
		List<ReminderDTO> reminders = null;
		try {
			ResponseEntity<List<ReminderDTO>> response = reminderController.getReminders();
			reminders = response.getBody();
		} catch (Exception e) {
			logger.error("Failed to load reminders", e);
		}

		if (reminders == null || reminders.isEmpty()) {
			return new TreeMap<>();
		}

		return reminders.stream().collect(
				Collectors.groupingBy(
						r -> r.getRemindAt().getYear(),
						TreeMap::new,
						Collectors.groupingBy(
								r -> r.getRemindAt().getMonth().name(),
								TreeMap::new,
								Collectors.toList()
						)
				)
		);
	}

	private void createTabs(Map<Integer, Map<String, List<ReminderDTO>>> grouped) {
		for (Map.Entry<Integer, Map<String, List<ReminderDTO>>> yearEntry : grouped.entrySet()) {
			int year = yearEntry.getKey();

			// Disabled year header tab
			monthTabbedPane.addTab("── " + year + " ──", null, new JPanel(), null);
			monthTabbedPane.setEnabledAt(monthTabbedPane.getTabCount() - 1, false);

			// Month tabs under the year header
			for (Map.Entry<String, List<ReminderDTO>> monthEntry : yearEntry.getValue().entrySet()) {
				String monthName = monthEntry.getKey();
				List<ReminderDTO> reminders = monthEntry.getValue();

				JPanel listPanel = new JPanel();
				listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

				for (ReminderDTO reminder : reminders) {
					TaskDTO task = null;
					try {
						ResponseEntity<TaskDTO> response = taskController.getTaskById(reminder.getTaskId());
						task = response.getBody();
					} catch (Exception e) {
						logger.error("Failed to load task for reminder", e);
					}

					ReminderRowPanel rowPanel = new ReminderRowPanel(reminder, task);
					listPanel.add(rowPanel);
					listPanel.add(Box.createVerticalStrut(2));
				}

				listPanel.add(Box.createVerticalGlue());

				JScrollPane scrollPane = new JScrollPane(listPanel);
				scrollPane.getVerticalScrollBar().setUnitIncrement(16);

				String displayName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
				monthTabbedPane.addTab(displayName, scrollPane);
			}
		}
//		right after the first year value
		monthTabbedPane.setSelectedIndex(1);
	}
}
