package app.gui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import app.dco.ProjectDCO;
import app.entities.IconColor;
import app.services.CreateProjectService;
import app.services.GetIconColorsService;

public class CreateProjectWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField projectTitleTextField;
	private JTextArea descriptionTextArea;
	private ButtonGroup bg;
	private static final Logger logger = Logger.getLogger(CreateProjectWindow.class.getName());

	/**
	 * Create the dialog.
	 */
	public CreateProjectWindow(JFrame source) {
		logger.info("Drawing the window.");
		Dimension sourceSize = source.getSize();
//		Make the size of this window to be half of size of main window
		setSize(new Dimension((int)(sourceSize.getWidth() / 2), (int)(sourceSize.getHeight() / 2)));
		setLocationRelativeTo(source);
		setResizable(false);
//		Can't interact with JFrame until JDialog is closed
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel titlePanel = new JPanel();
		contentPanel.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		
		projectTitleTextField = new JTextField();
		projectTitleTextField.setToolTipText("Project Title");
		projectTitleTextField.setFont(new Font("Dialog", Font.PLAIN, 20));
		projectTitleTextField.setBackground(Color.WHITE);
		titlePanel.add(projectTitleTextField);
		projectTitleTextField.setColumns(10);

		JPanel descriptionPanel = new JPanel();
		contentPanel.add(descriptionPanel, BorderLayout.CENTER);
		descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.X_AXIS));
		
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setFont(new Font("Dialog", Font.PLAIN, 20));
		descriptionTextArea.setColumns(40);
		descriptionTextArea.setRows(25);
		descriptionTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String text = descriptionTextArea.getText();
				if(text.length() >= 1000) {
					descriptionTextArea.setText(text.substring(0, 1000));
				}
			}
		});
		descriptionTextArea.setBackground(Color.GRAY);
		
		descriptionPanel.add(descriptionTextArea);

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
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		JButton createButton = new JButton("CREATE");
		createButton.setActionCommand("CREATE");
		buttonPane.add(createButton);
		getRootPane().setDefaultButton(createButton);
				
		listColors(colorRadioPanel);
		addCreateButtonActionListener(createButton);
		addCancelButtonActionListener(cancelButton);
		
		revalidate();
		repaint();
		setVisible(true);

		logger.info("Window is ready.");
	}
	
	private void addCancelButtonActionListener(JButton button) {
		logger.info("Running function.");
		button.addActionListener(_ -> {
			dispose();
		});
	}
	
	private void addCreateButtonActionListener(JButton button) {
		logger.info("Running function.");
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
			
			if(CreateProjectService.execute(new ProjectDCO(title, description, iconColorId))) {
				JOptionPane.showMessageDialog(new JDialog(), "Project Created Succesfully");
				dispose();
			}
			else {
				JOptionPane.showMessageDialog(new JDialog(), "Error while creating project");
			}
		});
	}
	
	private void listColors(Container container) {
		logger.info("Running function.");

		List<IconColor> ic = GetIconColorsService.execute();
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
		logger.info("Running function.");

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
