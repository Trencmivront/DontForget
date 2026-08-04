package main.io.github.trencmivront.dontforget.gui.panels.rows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.io.github.trencmivront.dontforget.controllers.ReminderController;
import main.io.github.trencmivront.dontforget.custom.SpringContext;
import main.io.github.trencmivront.dontforget.dto.ReminderDTO;
import main.io.github.trencmivront.dontforget.dto.TaskDTO;
import main.io.github.trencmivront.dontforget.gui.Main;
import main.io.github.trencmivront.dontforget.gui.popups.ReminderDialog;

public class ReminderRowPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ReminderRowPanel.class.getName());

	private ReminderController reminderController;
	private TaskDTO taskDTO;
	
	public ReminderRowPanel(ReminderDTO reminder, TaskDTO taskDTO) {
		logger.info("Initializing ReminderRowPanel");
		this.reminderController = SpringContext.getBean(ReminderController.class);
		this.taskDTO = taskDTO;
		setLayout(new BorderLayout(8, 0));
		setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
		setAlignmentX(LEFT_ALIGNMENT);

		LocalDateTime localDateTime = reminder.getRemindAt();
		if (localDateTime != null) {
			// Left panel: day number on top, time below
			JPanel datePanel = new JPanel(new GridLayout(2, 1, 0, 0));
			datePanel.setOpaque(false);

			JLabel dayLabel = new JLabel(
					localDateTime.getDayOfMonth() + " " +
					localDateTime.getMonth().name().substring(0, 3),
					SwingConstants.CENTER);
			dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD, 11f));

			JLabel timeLabel = new JLabel(
					localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
					SwingConstants.CENTER);
			timeLabel.setFont(timeLabel.getFont().deriveFont(Font.PLAIN, 10f));

			datePanel.add(dayLabel);
			datePanel.add(timeLabel);
			add(datePanel, BorderLayout.WEST);
		}

		JLabel title = new JLabel(reminder.getMessage() == null || reminder.getMessage().isBlank() ? taskDTO.getTaskTitle():reminder.getMessage());
		title.setHorizontalAlignment(SwingConstants.LEFT);
		add(title, BorderLayout.CENTER);

		addMouseListeners();
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
	}

	private void addMouseListeners() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		
		popupMenu.add(deleteItem);
		
		deleteItem.addActionListener(_ -> {
			Long taskId = taskDTO.getTaskId();
			reminderController.deleteReminder(taskId);
			Main.getMain().getRemindersButton().doClick();
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) { // Left click
					new ReminderDialog(taskDTO.getTaskId());
				} else if (e.getButton() == MouseEvent.BUTTON3) { // Right click
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
}
