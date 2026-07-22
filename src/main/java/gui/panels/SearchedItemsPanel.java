package main.java.gui.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import main.java.controllers.ProjectController;
import main.java.controllers.TagController;
import main.java.controllers.TaskController;
import main.java.custom.SpringContext;
import main.java.entities.Project;
import main.java.entities.Tag;
import main.java.entities.Task;
import main.java.gui.windows.SearchWindow;

public class SearchedItemsPanel extends JPanel{

	private static final Logger logger = LoggerFactory.getLogger(SearchedItemsPanel.class.getName());

	private ProjectController projectController;
	private TaskController taskController;
	private TagController tagController;
	
	private static final long serialVersionUID = 1L;
	private final JTable itemsTable = new JTable();
	private DefaultTableModel model;

	private Window source;
	
	public SearchedItemsPanel(SearchWindow source) {
		this.source = source;
		logger.info("Initializing SearchedItemsPanel");
		this.projectController = SpringContext.getBean(ProjectController.class);
		this.taskController = SpringContext.getBean(TaskController.class);
		this.tagController = SpringContext.getBean(TagController.class);
		
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
		try {
			ResponseEntity<List<Project>> response = projectController.getProjects();
			List<Project> projects = response.getBody();
			
			if (projects != null && !projects.isEmpty()) {
				model.addRow(new Object[] { createHeader("Projects") });
				for (Project project : projects) {
					ProjectRowPanel row = new ProjectRowPanel(project);
					model.addRow(new Object[] { row });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void listTasks() {
		try {
			ResponseEntity<List<Task>> response = taskController.getTasks();
			List<Task> tasks = response.getBody();
			if (tasks != null && !tasks.isEmpty()) {
				model.addRow(new Object[] { createHeader("Tasks") });
				for (Task task : tasks) {
					TaskRowPanel row = new TaskRowPanel(task, source);
					List<Tag> tags = null;
					try {
						ResponseEntity<List<Tag>> tagsResponse = tagController.getTagsOfTask(task.taskId());
						tags = tagsResponse.getBody();
					} catch (Exception ex) {
						logger.error("Failed to load tags for task", ex);
					}
					if (tags != null && !tags.isEmpty()) {
						StringBuilder tagsBuilder = new StringBuilder();
						for (Tag tag : tags) {
							tagsBuilder.append(" ").append(tag.tagName());
						}
						row.setToolTipText(tagsBuilder.toString());
					}
					model.addRow(new Object[] { row });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void listTags() {
		try {
			ResponseEntity<List<Tag>> response = tagController.getTags();
			List<Tag> tags = response.getBody();
			if (tags != null && !tags.isEmpty()) {
				model.addRow(new Object[] { createHeader("Tags") });
				for (Tag tag : tags) {
					TagRowPanel row = new TagRowPanel(tag);
					model.addRow(new Object[] { row });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
