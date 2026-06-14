package app.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import app.dco.ProjectDCO;
import app.entities.IconColor;
import app.services.CreateProjectService;
import app.services.GetIconColorsService;
import app.enums.LightColors;

import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTextArea;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JScrollPane;

public class CreateProjectWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField projectTitleTextField;
	private JTextArea descriptionTextArea;
	private ButtonGroup bg;
	private static Connection conn;

	/**
	 * Create the dialog.
	 */
	public CreateProjectWindow(JFrame source , Connection conn) {
		CreateProjectWindow.conn = conn;
				
		setBounds(100, 100, 450, 300);
//		Locate the dialog at the center of JFrame
		setLocationRelativeTo(source);
		setResizable(false);
//		Can't interact with JFrame until JDialog is closed
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(LightColors.BACKGROUND.getColor());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(LightColors.BACKGROUND.getColor());
		contentPanel.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

		JLabel projectTitleLabel = new JLabel("Title");
		projectTitleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		projectTitleLabel.setForeground(LightColors.TEXT_MAIN.getColor());
		titlePanel.add(projectTitleLabel);

		projectTitleTextField = new JTextField();
		projectTitleTextField.setToolTipText("Project Title");
		projectTitleTextField.setFont(new Font("Dialog", Font.PLAIN, 20));
		projectTitleTextField.setForeground(LightColors.TEXT_MAIN.getColor());
		projectTitleTextField.setBackground(Color.WHITE);
		titlePanel.add(projectTitleTextField);
		projectTitleTextField.setColumns(10);

		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setBackground(LightColors.BACKGROUND.getColor());
		contentPanel.add(descriptionPanel, BorderLayout.CENTER);
		descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.X_AXIS));

		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		descriptionLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		descriptionLabel.setForeground(LightColors.TEXT_MAIN.getColor());
		descriptionPanel.add(descriptionLabel);
		
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setFont(new Font("Dialog", Font.PLAIN, 20));
		descriptionTextArea.setColumns(15);
		descriptionTextArea.setForeground(LightColors.TEXT_MAIN.getColor());
		descriptionTextArea.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setBorder(new EmptyBorder(20, 10, 20, 10));
		scrollPane.getViewport().setBackground(Color.WHITE);
		
		descriptionPanel.add(scrollPane);


		JPanel colorPanel = new JPanel();
		colorPanel.setBackground(LightColors.BACKGROUND.getColor());
		contentPanel.add(colorPanel, BorderLayout.SOUTH);
		colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
		
		JLabel colorLabel = new JLabel("Color: ");
		colorLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		colorLabel.setForeground(LightColors.TEXT_MAIN.getColor());
		colorPanel.add(colorLabel);
		
		JPanel colorRadioPanel = new JPanel();
		colorRadioPanel.setBackground(LightColors.BACKGROUND.getColor());
		colorPanel.add(colorRadioPanel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBackground(LightColors.BACKGROUND.getColor());
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.setBackground(LightColors.BUTTON_HOVER.getColor());
		cancelButton.setForeground(LightColors.TEXT_MAIN.getColor());
		buttonPane.add(cancelButton);

		JButton createButton = new JButton("CREATE");
		createButton.setBackground(LightColors.PRIMARY.getColor());
		createButton.setForeground(LightColors.BACKGROUND.getColor());
		createButton.setActionCommand("CREATE");
		buttonPane.add(createButton);
		getRootPane().setDefaultButton(createButton);
		
		setVisible(true);
		
		listColors(colorRadioPanel);
		addCreateButtonActionListener(createButton);
		addCancelButtonActionListener(cancelButton);
			
	}
	
	private void addCancelButtonActionListener(JButton button) {
		button.addActionListener(_ -> {
			dispose();
		});
	}
	
	private void addCreateButtonActionListener(JButton button) {
		button.addActionListener(_ -> {
			String title = projectTitleTextField.getText();
			String description = descriptionTextArea.getText(); // Description can be null
			// we don't want the title to be empty
			if(title == null || title.equals("")) {
				JOptionPane.showMessageDialog(new JDialog(), "Please write a title for your project.");
				return;
			}
			JRadioButton selectedRadioButton = getSelectedRadioButton(bg);
//			if color is not picked, make color red by default
			int iconColorId = 1;
			if(selectedRadioButton != null) {
				iconColorId = (int) selectedRadioButton.getClientProperty("icon_color_id");
			}
			
			if(CreateProjectService.execute(conn, new ProjectDCO(title, description, iconColorId))) {
				JOptionPane.showMessageDialog(new JDialog(), "Project Created Succesfully");
				dispose();
			}
			else {
				JOptionPane.showMessageDialog(new JDialog(), "Error while creating project");
			}
		});
	}
	
	private void listColors(Container container) {
		
		List<IconColor> ic = GetIconColorsService.execute(conn);
		// initialize the ButtonGroup here
		bg = new ButtonGroup();
		
		for(IconColor color : ic) {
			
			JRadioButton rb = new JRadioButton();
			rb.setActionCommand(Integer.toString(color.icon_color_id()));
			rb.setBackground(new Color(color.red(), color.green(), color.blue()));
			rb.putClientProperty("icon_color_id", color.icon_color_id());
			bg.add(rb);
			container.add(rb);
		}
		
		bg.setSelected(bg.getSelection(), true);
				
	}
	
	// a function for getting selected radio button from a button group
	private JRadioButton getSelectedRadioButton(ButtonGroup group) {
	    // Get all buttons added to the group
	    Enumeration<AbstractButton> buttons = group.getElements();
	    
	    while (buttons.hasMoreElements()) {
	        JRadioButton button = (JRadioButton) buttons.nextElement();
	        if (button.isSelected()) {
	            return button; // Return the selected button component
	        }
	    }
	    return null; // Return null if no button is selected
	}

}
