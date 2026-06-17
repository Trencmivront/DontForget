package app.gui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Timestamp;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class TaskWindow extends JDialog {
	
	private static final long serialVersionUID = 1L;
 	private JPanel contentPanel;
	private JPanel detailsPanel;
	private JPanel taskPanel;
	private JPanel descriptionPanel;
	private static final Logger logger = Logger.getLogger(TaskWindow.class.getName());

	public TaskWindow(JPanel source, JPanel taskPanel) {
		logger.info("Drawing the window.");
		this.taskPanel = taskPanel;
		
		Dimension sourceSize = source.getSize();
		int width = (int) (sourceSize.getWidth() * 0.55);
		int height = (int) (sourceSize.getHeight() * 0.55);
		if (width < 500) width = 500;
		if (height < 400) height = 400;
		setSize(new Dimension(width, height));
		setLocationRelativeTo(source);
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// Left: Details Panel
		detailsPanel = new JPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
		
		// Main Content Panel
		contentPanel = new JPanel(new BorderLayout(10, 10));
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		// Right: Description Panel
		descriptionPanel = new JPanel(new BorderLayout());
		descriptionPanel.setBorder(new TitledBorder(null, "Description", TitledBorder.LEADING, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 14), Color.DARK_GRAY));
		
		// Split Layout Panel (Side-by-side Details and Description)
		JPanel splitPanel = new JPanel(new BorderLayout(15, 0));
		splitPanel.add(detailsPanel, BorderLayout.WEST);
		splitPanel.add(descriptionPanel, BorderLayout.CENTER);
		contentPanel.add(splitPanel, BorderLayout.CENTER);
		
		// Bottom Button Panel
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton closeButton = new JButton("Close");
		closeButton.setFont(new Font("Dialog", Font.BOLD, 14));
		closeButton.addActionListener(_ -> dispose());
		buttonPane.add(closeButton);
		getRootPane().setDefaultButton(closeButton);
		
		addLeftContent();
		addRightContent();
		
		revalidate();
		repaint();
		setVisible(true);
		
		logger.info("Window is ready.");
	}
	
	private void addLeftContent() {
		
		// Extract properties
		Integer statusId = (Integer) taskPanel.getClientProperty("status_id");
		Integer priority = (Integer) taskPanel.getClientProperty("priority");
		Timestamp dueDate = (Timestamp) taskPanel.getClientProperty("due_date");
		Timestamp completedAt = (Timestamp) taskPanel.getClientProperty("completed_at");
		
		String taskTitle = "Task Details";
		for (Component c : taskPanel.getComponents()) {
			if (c instanceof JLabel) {
				taskTitle = ((JLabel) c).getText();
				break;
			}
		}
		
		// Title label
		JLabel titleLabel = new JLabel(taskTitle);
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 22));
		titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
		contentPanel.add(titleLabel, BorderLayout.NORTH);
				
		String statusStr = "Unknown";
		Color statusColor = Color.BLACK;
		if (statusId != null) {
			if (statusId == 1) {
				statusStr = "ACTIVE";
				statusColor = new Color(40, 167, 69);
			} else if (statusId == 2) {
				statusStr = "COMPLETED";
				statusColor = new Color(0, 123, 255);
			} else if (statusId == 3) {
				statusStr = "PAST";
				statusColor = new Color(108, 117, 125);
			} else {
				statusStr = "Status " + statusId;
			}
		}
		
		String priorityStr = "None";
		Color priorityColor = Color.BLACK;
		if (priority != null) {
			if (priority == 1) {
				priorityStr = "High (1)";
				priorityColor = new Color(220, 53, 69);
			} else if (priority == 2) {
				priorityStr = "Medium (2)";
				priorityColor = new Color(255, 140, 0);
			} else if (priority == 3) {
				priorityStr = "Low (3)";
				priorityColor = new Color(40, 167, 69);
			} else {
				priorityStr = priority.toString();
			}
		}
		
		addDetailRow("Status:", statusStr, statusColor);
		addDetailRow("Priority:", priorityStr, priorityColor);
		addDetailRow("Due Date:", dueDate != null ? dueDate.toString() : "No due date", Color.BLACK);
		if (completedAt != null) {
			addDetailRow("Completed At:", completedAt.toString(), Color.BLACK);
		}

	}
	
	private void addRightContent() {
		
		String description = (String) taskPanel.getClientProperty("description");
		
		JTextArea descTextArea = new JTextArea();
		descTextArea.setEditable(true);
		descTextArea.setLineWrap(true);
		descTextArea.setWrapStyleWord(true);
		descTextArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		descTextArea.setText(description != null ? description : "");
		descTextArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JScrollPane descScrollPane = new JScrollPane(descTextArea);
		descriptionPanel.add(descScrollPane, BorderLayout.CENTER);
		
	}
	
	private void addDetailRow(String labelText, String valueText, Color valueColor) {
		JLabel lbl = new JLabel(labelText);
		lbl.setFont(new Font("Dialog", Font.BOLD, 14));

		JLabel valLbl = new JLabel(valueText);
		valLbl.setFont(new Font("Dialog", Font.PLAIN, 14));
				
		JPanel detailSet = new JPanel();
		detailSet.setLayout(new GridLayout(0, 2, 10, 5));
		
		detailSet.add(lbl);
		detailSet.add(valLbl);
		
		detailSet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		detailsPanel.add(detailSet);

	}
}
