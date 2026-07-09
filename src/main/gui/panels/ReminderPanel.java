package main.gui.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.ListSelectionEvent;

import com.github.lgooddatepicker.zinternaltools.WrapLayout;

import main.entities.Reminder;
import main.entities.Task;
import main.gui.Main;
import main.gui.windows.CreateUpdateTaskWindow;
import main.services.reminder.GetRemindersService;
import main.services.task.GetTaskByIdService;

public class ReminderPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	private JTabbedPane monthTabbedPane;
	
	private static final Logger logger = Logger.getLogger(ReminderPanel.class.getName());
	
	public ReminderPanel() {
		logger.info("Drawing the Reminder panel.");
		setLayout(new BorderLayout());
		
		monthTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		monthTabbedPane.setSelectedIndex(-1);
		
		addTabs();
		
		add(new HeaderPanel("Reminders"), BorderLayout.NORTH);
		add(monthTabbedPane, BorderLayout.CENTER);
		logger.info("Window is ready.");
	}
		
	private void addTabs(){		
		
		HashMap<String, List<Reminder>> monthlyReminders = getMonthlyReminders();
		
		if(monthlyReminders == null) {
			monthTabbedPane.addTab("Empty", new EmptyPanel("You don't have any reminder."));
			return;
		}
		
		createTab(monthlyReminders);
	}
	
	private HashMap<String, List<Reminder>> getMonthlyReminders() {
		List<Reminder> reminders = GetRemindersService.execute();
		
		if(reminders.isEmpty() || reminders == null) {
			return null;
		}
		
		HashMap<String, List<Reminder>> monthReminderHash = new HashMap<String, List<Reminder>>();
		
		for(Reminder reminder : reminders) {
			
			LocalDateTime localDateTime = reminder.remind_at().toLocalDateTime();
			String monthName = localDateTime.getMonth().name();
			
			// if month has not been set yet, create a month with new list
			if(monthReminderHash.get(monthName) == null) {
				List<Reminder> r = new ArrayList<Reminder>();
				r.add(reminder);
				monthReminderHash.putIfAbsent(monthName, r);
			}else {
				// add reminder to existing month
				monthReminderHash.get(monthName).add(reminder);
			}
		}
		return monthReminderHash;
	}
	
	private void createTab(HashMap<String, List<Reminder>> hashMap){
		GetTaskByIdService taskService = new GetTaskByIdService();
		
		for (String monthName : hashMap.keySet()) {
			List<Reminder> reminders = hashMap.get(monthName);
			
			DefaultTableModel model = new DefaultTableModel(new Object[] { "Day", "Task", "Action" }, 0) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return column == 2;
				}
				
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					if (columnIndex == 0) {
						return Integer.class;
					}
					return String.class;
				}
			};
			
			for (Reminder reminder : reminders) {
				LocalDateTime localDateTime = reminder.remind_at().toLocalDateTime();
				int day = localDateTime.getDayOfMonth();
				
				Task task = taskService.execute(reminder.task_id());
				String taskTitle = (task != null) ? task.task_title() : "Unknown Task";
				
				model.addRow(new Object[] { day, taskTitle, "" });
			}
			
			JTable table = new JTable(model);
			table.setRowHeight(35);
			table.setFillsViewportHeight(true);
			
			// Adjust column widths
			table.getColumnModel().getColumn(0).setPreferredWidth(60);
			table.getColumnModel().getColumn(1).setPreferredWidth(400);
			table.getColumnModel().getColumn(2).setPreferredWidth(180);
						
			// Adjust row height when action column is too narrow to fit buttons in a single line
			table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
				private final int minSingleRowWidth = new ActionPanel().getPreferredSize().width;

				@Override
				public void columnMarginChanged(ChangeEvent e) {
					int actionColWidth = table.getColumnModel().getColumn(2).getWidth();
					int newRowHeight = (actionColWidth < minSingleRowWidth) ? 70 : 35;
					if (table.getRowHeight() != newRowHeight) {
						table.setRowHeight(newRowHeight);
					}
				}
//				These are useless
				@Override public void columnAdded(TableColumnModelEvent e) {}
				@Override public void columnRemoved(TableColumnModelEvent e) {}
				@Override public void columnMoved(TableColumnModelEvent e) {}
				@Override public void columnSelectionChanged(ListSelectionEvent e) {}
			});
			
			// Set cell renderer and editor for action column
			table.getColumnModel().getColumn(2).setCellRenderer(new ActionCellRenderer());
			table.getColumnModel().getColumn(2).setCellEditor(new ActionCellEditor());
			
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
			
			// Click listener to open TaskWindow
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int row = table.rowAtPoint(e.getPoint());
					int col = table.columnAtPoint(e.getPoint());
					if (row >= 0 && col == 1) {
						int modelRow = table.convertRowIndexToModel(row);
						Reminder reminder = reminders.get(modelRow);
						Task task = taskService.execute(reminder.task_id());
						if (task != null) {
							TaskRowPanel taskPanel = new TaskRowPanel(null, task);
							taskPanel.setLayout(new BorderLayout());
							JLabel title = new JLabel(task.task_title());
							taskPanel.putClientProperty("task_title", task.task_title());
							taskPanel.putClientProperty("task_id", task.task_id());
							taskPanel.putClientProperty("description", task.description());
							taskPanel.putClientProperty("status_id", task.status_id());
							taskPanel.putClientProperty("priority", task.priority());
							taskPanel.putClientProperty("due_date", task.due_date());
							taskPanel.putClientProperty("list_order", task.list_order());
							taskPanel.putClientProperty("project_id", task.project_id());
							taskPanel.putClientProperty("created_at", task.created_at());
							taskPanel.putClientProperty("updated_at", task.updated_at());
							taskPanel.putClientProperty("completed_at", task.completed_at());
							taskPanel.add(title, BorderLayout.CENTER);
							
							new CreateUpdateTaskWindow(Main.getMain(), task.project_id(), true, taskPanel);
						}
					}
				}
			});
			
			JScrollPane scrollPane = new JScrollPane(table);
			
			// Capitalize month name nicely (e.g. "JANUARY" -> "January")
			String displayName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
			monthTabbedPane.addTab(displayName, scrollPane);
		}
	}

	private static class ActionPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final JButton editButton = new JButton("Edit");
		private final JButton deleteButton = new JButton("Delete");

		public ActionPanel() {
			setLayout(new WrapLayout(WrapLayout.CENTER, 5, 1));
			add(editButton);
			add(deleteButton);
		}
	}

	private static class ActionCellRenderer implements TableCellRenderer {
		private final ActionPanel panel = new ActionPanel();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				panel.setBackground(table.getSelectionBackground());
			} else {
				panel.setBackground(table.getBackground());
			}
			return panel;
		}
	}

	private static class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		private final ActionPanel panel = new ActionPanel();

		public ActionCellEditor() {
			panel.editButton.addActionListener(_ -> {
				// Logic for update button will be written later
				fireEditingStopped();
			});
			panel.deleteButton.addActionListener(_ -> {
				// Logic for delete button will be written later
				fireEditingStopped();
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			panel.setBackground(table.getSelectionBackground());
			return panel;
		}

		@Override
		public Object getCellEditorValue() {
			return "";
		}
	}
}
