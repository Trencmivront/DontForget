package main.gui.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import main.entities.Project;
import main.gui.Main;
import main.gui.windows.SearchWindow;
import main.services.project.GetProjectsService;

public class SearchedItemsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private final JTable itemsTable = new JTable();
	private DefaultTableModel model;
	private SearchWindow window;
	
	public SearchedItemsPanel(SearchWindow window) {
		this.window = window;
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane(itemsTable);
		add(scrollPane, BorderLayout.CENTER);
		
		listItems();
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
						if (value instanceof JPanel) {
							JPanel panel = (JPanel) value;
							for (Component comp : panel.getComponents()) {
								if (comp instanceof JLabel) {
									String text = ((JLabel) comp).getText();
									if (text != null && text.toLowerCase().contains(lowerKey)) {
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
	
	private void listItems() {		
		List<Project> projects = GetProjectsService.execute();
		if (projects == null || projects.isEmpty()) {
			return;
		}
		
		model = new DefaultTableModel(new Object[] { "Projects" }, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		for (Project project : projects) {
			model.addRow(new Object[] { new ProjectRowPanel(null, project) });
		}
		
		itemsTable.setModel(model);
		itemsTable.setRowHeight(35);
		itemsTable.setFillsViewportHeight(true);
		
		itemsTable.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof Component) {
					Component c = (Component) value;
					if (isSelected) {
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
					} else {
						c.setBackground(table.getBackground());
						c.setForeground(table.getForeground());
					}
					return c;
				}
				return null;
			}
		});
		
		itemsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = itemsTable.rowAtPoint(e.getPoint());
				if (row >= 0) {
					int modelRow = itemsTable.convertRowIndexToModel(row);
					Object value = model.getValueAt(modelRow, 0);
					if (value instanceof ProjectRowPanel) {
						ProjectRowPanel panel = (ProjectRowPanel) value;
						try {
							Field field = Main.class.getDeclaredField("showInfoPanel");
							field.setAccessible(true);
							JPanel showInfoPanel = (JPanel) field.get(Main.getMain());
							showInfoPanel.removeAll();
							showInfoPanel.add(new ProjectInfoPanel(panel));
							Main.getMain().refreshWindow();
							
							if (window instanceof JDialog) {
								window.dispose();
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
	}
	
}

