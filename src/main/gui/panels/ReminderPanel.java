package main.gui.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.entities.Reminder;
import main.entities.Task;
import main.services.reminder.GetRemindersService;
import main.services.task.GetTaskByIdService;

public class ReminderPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	private JTabbedPane monthTabbedPane;
	
	private static final Logger logger = Logger.getLogger(ReminderPanel.class.getName());
	
	public ReminderPanel() {
		logger.info("Drawing the Reminder panel.");
		setLayout(new BorderLayout());
		
		monthTabbedPane = new JTabbedPane(SwingConstants.LEFT);
		monthTabbedPane.setSelectedIndex(-1);
		
		addTabs();
		
		add(new HeaderPanel("Reminders"), BorderLayout.NORTH);
		add(monthTabbedPane, BorderLayout.CENTER);
		logger.info("Window is ready.");
	}
		
	private void addTabs(){		
		
		HashMap<String, List<Reminder>> monthlyReminders = getMonthlyReminders();
		
		if(monthlyReminders.isEmpty()) {
			monthTabbedPane.addTab("Empty", new EmptyPanel("You don't have any reminder."));
			return;
		}
		
		createTab(monthlyReminders);
	}
	
	private HashMap<String, List<Reminder>> getMonthlyReminders() {
		List<Reminder> reminders = new GetRemindersService().execute();
		
		if(reminders.isEmpty()) {
			return new HashMap<>();
		}
//		Here we are grouping reminders by their month name, then for each key
//		we generate a list of reminders and put these key-value sets into a new HashMap.
//		Collectors.toList() function specifies that the values associated with a key must be inserted
//		as list of values mapped to that key. So the output will be instance of HashMap<String, List<Reminder>>.
//		The code line simply takes a list of items, groups them by given string, returns grouped values.
		return reminders.stream().
				collect(
				Collectors.groupingBy(reminder -> reminder.remind_at().toLocalDateTime().getMonth().name(),
				HashMap::new,
				Collectors.toList()
				));
	}
	
	private void createTab(HashMap<String, List<Reminder>> hashMap){
		GetTaskByIdService taskService = new GetTaskByIdService();
		
		for (Map.Entry<String, List<Reminder>> entry : hashMap.entrySet()) {
			String monthName = entry.getKey();
			List<Reminder> reminders = hashMap.get(monthName);
			
			DefaultTableModel model = new DefaultTableModel(new Object[] { "Day", "Task" }, 0) {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			
			for (Reminder reminder : reminders) {
				int dayInt = reminder.remind_at().toLocalDateTime().getDayOfMonth();

				Task task = taskService.execute(reminder.task_id());
				ReminderRowPanel rowPanel = new ReminderRowPanel(reminder, task);
				
				model.addRow(new Object[] { dayInt, rowPanel });
			}
			
			JTable table = new JTable(model);
			table.setRowHeight(35);
			table.setFillsViewportHeight(true);
			
			// Adjust column widths
			table.getColumnModel().getColumn(0).setPreferredWidth(60);
			table.getColumnModel().getColumn(1).setPreferredWidth(400);
						
			// Set cell renderer
			setTableCellRenderers(table);
			
			// Hover effect / cursor change for task title column
			table.addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					int row = table.rowAtPoint(e.getPoint());
					int col = table.columnAtPoint(e.getPoint());
					if (row >= 0 && col == 1) {
						table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else {
						table.setCursor(Cursor.getDefaultCursor());
					}
				}
			});
			
			// Forward mouse events (left click, right click, etc.) to the ReminderRowPanel in the cell
			addTableMouseListener(table);
			
			JScrollPane scrollPane = new JScrollPane(table);
			
			// Capitalize month name nicely (e.g. "JANUARY" -> "January")
			String displayName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
			monthTabbedPane.addTab(displayName, scrollPane);
		}
	}
	
	private void setTableCellRenderers(JTable table) {
//		Center the text
		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public int getHorizontalAlignment() {
				return SwingConstants.CENTER;
			}
		});
		
		table.getColumnModel().getColumn(1).setCellRenderer((tableRef, value, isSelected, _, _, _) -> {
			if (value instanceof Component c) {
				if (isSelected) {
					c.setBackground(tableRef.getSelectionBackground());
					c.setForeground(tableRef.getSelectionForeground());
				} else {
					c.setBackground(tableRef.getBackground());
					c.setForeground(tableRef.getForeground());
				}
				return c;
			}
			return null;
		});
	}
	
	private void addTableMouseListener(JTable table) {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseEvent(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseEvent(e);
			}

			private void handleMouseEvent(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				if (row >= 0 && col == 1) {
					Object value = table.getValueAt(row, 1);
					if (value instanceof JPanel panel) {
						panel.dispatchEvent(new MouseEvent(
							panel,
							e.getID(),
							e.getWhen(),
							e.getModifiersEx(),
							e.getX() - table.getCellRect(row, col, true).x,
							e.getY() - table.getCellRect(row, col, true).y,
							e.getClickCount(),
							e.isPopupTrigger(),
							e.getButton()
						));
					}
				}
			}
		});
	}
}
