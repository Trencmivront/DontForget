package app.gui.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.github.lgooddatepicker.zinternaltools.WrapLayout;

import app.entities.Inbox;
import app.services.inbox.DeleteMessageByIdService;
import app.services.inbox.GetInboxService;

public class InboxPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InboxPanel.class.getName());

	public InboxPanel() {
		logger.info("Initializing InboxPanel.");
		
		setLayout(new BorderLayout());

		List<Inbox> inboxItems = null;

		inboxItems = GetInboxService.execute();

		if (inboxItems == null || inboxItems.isEmpty()) {
			add(new EmptyPanel("Your inbox is empty."), BorderLayout.CENTER);
		} else {
			DefaultTableModel model = new DefaultTableModel(new Object[] { "Message", "Date", "Action" }, 0) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return column == 2;
				}
				
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return String.class;
				}
			};

			for (Inbox item : inboxItems) {
				model.addRow(new Object[] { item.message(), item.created_at().toString(), "" });
			}

			JTable table = new JTable(model);
			table.setRowHeight(35);
			table.setFillsViewportHeight(true);

			// Adjust column widths
			table.getColumnModel().getColumn(0).setPreferredWidth(400);
			table.getColumnModel().getColumn(1).setPreferredWidth(160);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);

			// Set cell renderer and editor for action column
			table.getColumnModel().getColumn(2).setCellRenderer(new ActionCellRenderer());
			table.getColumnModel().getColumn(2).setCellEditor(new ActionCellEditor(table, model, inboxItems));

			JScrollPane scrollPane = new JScrollPane(table);
			add(scrollPane, BorderLayout.CENTER);
		}

		logger.info("InboxPanel drawn.");
	}

	private static class ActionPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final JButton deleteButton = new JButton("Delete");

		public ActionPanel() {
			setLayout(new WrapLayout(FlowLayout.CENTER, 5, 0));
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

		public ActionCellEditor(JTable table, DefaultTableModel model, List<Inbox> inboxItems) {
			panel.deleteButton.addActionListener(_ -> {
				int row = table.getEditingRow();
				fireEditingStopped();
				if (row != -1) {
					int modelRow = table.convertRowIndexToModel(row);
					Inbox item = inboxItems.get(modelRow);
					DeleteMessageByIdService.execute(item.inbox_id().intValue());
					model.removeRow(modelRow);
					inboxItems.remove(modelRow);
				}
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
