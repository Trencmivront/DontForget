package main.gui.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SearchedItemsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private final JTable itemsTable = new JTable() {
		@Override
		public boolean isCellEditable(int row, int column) {return false;};
	};
	private DefaultTableModel model;
	
	public SearchedItemsPanel() {
		setLayout(new BorderLayout(0, 0));		
		
		JScrollPane scrollPane = new JScrollPane(itemsTable);
		add(scrollPane, BorderLayout.CENTER);
		
		listItems();
		
	}

	public void filterRows(String key) {
		
	}
	
	private void listItems() {
		
	}
	
}
