package main.gui.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import main.entities.Project;
import main.entities.Task;
import main.entities.Tag;
import main.gui.windows.SearchWindow;
import main.services.project.GetProjectsService;
import main.services.task.GetTasksService;
import main.services.tag.GetTagsService;
import main.services.tag.GetTagsOfTaskService;

public class SearchedItemsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private final JTable itemsTable = new JTable();
	private DefaultTableModel model;
	
	public SearchedItemsPanel(SearchWindow window) {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane(itemsTable);
		add(scrollPane, BorderLayout.CENTER);
		
		listItems();
		addRowMouseListener();
	}

	
	public void filterRows(String key) {
		if (model != null) {
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
			if (key == null || key.trim().isEmpty()) {
				sorter.setRowFilter(null);
			} else {
				String lowerKey = key.toLowerCase();
				sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
					@Override
					public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
						Object value = entry.getValue(0);
						if (value instanceof JPanel panel) {
							for (Component comp : panel.getComponents()) {
								if (comp instanceof JLabel label) {
									String text = label.getText();
									String toolTipText = ((JPanel)label.getParent()).getToolTipText();
									
									boolean isTitleMatch = text != null && text.toLowerCase().contains(lowerKey);
									boolean isToolTipMatch = toolTipText != null && toolTipText.toLowerCase().contains(lowerKey);

									if (isTitleMatch || isToolTipMatch) {
										return true;
									}
								}
							}
						}
						return false;
					}
				});
			}
			itemsTable.setRowSorter(sorter);
		}
	}
	
	private JPanel createHeader(String title) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setBorder(new EmptyBorder(3, 2, 3, 2));
		JLabel label = new JLabel(title);
		label.setFont(new Font("Dialog", Font.BOLD, 16));
		panel.add(label);
		return panel;
	}

	private void listItems() {		
		model = new DefaultTableModel(new Object[] { "Search Results" }, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		listProjects();
		listTasks();
		listTags();
		
		itemsTable.setModel(model);
		itemsTable.setRowHeight(35);
		itemsTable.setFillsViewportHeight(true);
		
		itemsTable.getColumnModel().getColumn(0).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
			if (value instanceof Component c) {
				if(hasFocus) {
					c.setBackground(table.getSelectionBackground());
					c.setForeground(table.getSelectionForeground());
				} else {
					c.setBackground(table.getBackground());
					c.setForeground(table.getForeground());
				}
				return c;
			}
			return null;
		});
		
	}
	
	private void listProjects() {
		List<Project> projects = new GetProjectsService().execute();
		if (projects != null && !projects.isEmpty()) {
			model.addRow(new Object[] { createHeader("Projects") });
			for (Project project : projects) {
				ProjectRowPanel row = new ProjectRowPanel(project);
				model.addRow(new Object[] { row });
			}
		}
	}

	private void listTasks() {
		List<Task> tasks = new GetTasksService().execute();
		if (tasks != null && !tasks.isEmpty()) {
			model.addRow(new Object[] { createHeader("Tasks") });
			for (Task task : tasks) {
				TaskRowPanel row = new TaskRowPanel(task);
				List<Tag> tags = new GetTagsOfTaskService().execute(task.task_id());
				if (tags != null && !tags.isEmpty()) {
					StringBuilder tagsBuilder = new StringBuilder();
					for (Tag tag : tags) {
						tagsBuilder.append(" ").append(tag.tag_name());
					}
					row.setToolTipText(tagsBuilder.toString());
				}
				model.addRow(new Object[] { row });
			}
		}
	}

	private void listTags() {
		List<Tag> tags = new GetTagsService().execute();
		if (tags != null && !tags.isEmpty()) {
			model.addRow(new Object[] { createHeader("Tags") });
			for (Tag tag : tags) {
				TagRowPanel row = new TagRowPanel(tag);
				model.addRow(new Object[] { row });
			}
		}
	}
	
	private void addRowMouseListener(){
		itemsTable.addMouseListener(new MouseAdapter() {
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
				int row = itemsTable.rowAtPoint(e.getPoint());
				if (row >= 0) {
					if (itemsTable.getSelectedRow() != row) {
						itemsTable.setRowSelectionInterval(row, row);
					}
					Object value = itemsTable.getValueAt(row, 0);
					if (value instanceof JPanel panel) {
						panel.dispatchEvent(new MouseEvent(
							itemsTable,
							e.getID(),
							e.getWhen(),
							e.getModifiersEx(),
							e.getX(),
							e.getY(),
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

