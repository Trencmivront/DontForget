package app.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import java.awt.Component;
import javax.swing.JScrollPane;

public class CreateProjectWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField projectTitleTextField;
	private JTextArea descriptionTextArea;

	/**
	 * Create the dialog.
	 */
	public CreateProjectWindow() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel titlePanel = new JPanel();
		contentPanel.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

		JLabel projectTitleLabel = new JLabel("Title");
		projectTitleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		titlePanel.add(projectTitleLabel);

		projectTitleTextField = new JTextField();
		projectTitleTextField.setToolTipText("Project Title");
		projectTitleTextField.setFont(new Font("Dialog", Font.PLAIN, 20));
		titlePanel.add(projectTitleTextField);
		projectTitleTextField.setColumns(10);

		JPanel descriptionPanel = new JPanel();
		contentPanel.add(descriptionPanel, BorderLayout.CENTER);
		descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.X_AXIS));

		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		descriptionLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		descriptionPanel.add(descriptionLabel);
		
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setFont(new Font("Dialog", Font.PLAIN, 20));
		descriptionTextArea.setColumns(15);

		JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		descriptionPanel.add(scrollPane);


		JPanel colorPanel = new JPanel();
		contentPanel.add(colorPanel, BorderLayout.SOUTH);
		colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
		
		JLabel colorLabel = new JLabel("Color: ");
		colorLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		colorPanel.add(colorLabel);
		
		JPanel colorRadioPanel = new JPanel();
		colorPanel.add(colorRadioPanel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton createButton = new JButton("CREATE");
		createButton.setBackground(new Color(143, 240, 164));
		createButton.setForeground(new Color(36, 31, 49));
		createButton.setActionCommand("CREATE");
		buttonPane.add(createButton);
		getRootPane().setDefaultButton(createButton);
		addCreateButtonActionListener(createButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		setVisible(true);
			
	}
	
	private void addCreateButtonActionListener(JButton button) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String title = projectTitleTextField.getText();
				String description = descriptionTextArea.getText();
				// we don't want the title to be empty
				if(title == null || title.equals("")) {
					JOptionPane.showMessageDialog(new JDialog(), "Please write a title for your project.");
					return;
				}
				
				
				
			}
		});
	}

}
