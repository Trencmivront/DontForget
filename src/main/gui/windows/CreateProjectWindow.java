package main.gui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import main.gui.popup.ErrorDialog;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.dco.ProjectDCO;
import main.entities.IconColor;
import main.services.icon.GetIconColorsService;
import main.services.project.CreateProjectService;

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
		
		// Find active Main window to anchor to
		Window activeWindow = source;
		if (activeWindow == null) {
			activeWindow = FocusManager.getCurrentManager().getActiveWindow();
		}
		if (activeWindow == null) {
			for (Frame f : Frame.getFrames()) {
				if (f.isVisible()) {
					activeWindow = f;
					break;
				}
			}
		}

		setTitle("Create Project");
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		if (activeWindow != null) {
			Dimension size = activeWindow.getSize();
			int w = Math.min(480, (int) (size.getWidth() * 0.75));
			int h = Math.min(400, (int) (size.getHeight() * 0.75));
			setSize(new Dimension(w, h));
			setLocationRelativeTo(activeWindow);
		} else {
			setSize(new Dimension(480, 400));
			setLocation(200, 200);
		}

		// Content Panel with standard margin
		contentPanel.setLayout(new BorderLayout(15, 15));
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// 1. Project Title (Header Panel)
		projectTitleTextField = new JTextField();
		projectTitleTextField.setFont(new Font("Dialog", Font.BOLD, 15));
		projectTitleTextField.putClientProperty("JTextField.placeholderText", "Title of the project");
		projectTitleTextField.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		contentPanel.add(projectTitleTextField, BorderLayout.NORTH);

		// 2. Center Panel (Description + Options/Colors)
		JPanel centerPanel = new JPanel(new BorderLayout(0, 12));

		descriptionTextArea = new JTextArea();
		descriptionTextArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.putClientProperty("JTextArea.placeholderText", "Add details or description...");
		descriptionTextArea.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		
		descriptionTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String text = descriptionTextArea.getText();
				if(text.length() >= 1000) {
					descriptionTextArea.setText(text.substring(0, 1000));
				}
			}
		});

		JScrollPane descScrollPane = new JScrollPane(descriptionTextArea);
		centerPanel.add(descScrollPane, BorderLayout.CENTER);

		// Color Panel under description (replaces task options Panel)
		JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		colorPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

		JLabel colorLabel = new JLabel("Color: ");
		colorLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		colorPanel.add(colorLabel);

		JPanel colorRadioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		colorPanel.add(colorRadioPanel);

		centerPanel.add(colorPanel, BorderLayout.SOUTH);
		contentPanel.add(centerPanel, BorderLayout.CENTER);

		// 3. Footer Panel (Cancel + Create buttons)
		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Dialog", Font.PLAIN, 14));
		cancelButton.putClientProperty("JButton.buttonType", "roundRect");

		JButton createButton = new JButton("CREATE");
		createButton.setFont(new Font("Dialog", Font.BOLD, 14));
		createButton.putClientProperty("JButton.buttonType", "roundRect");

		buttonPane.add(cancelButton);
		buttonPane.add(createButton);
		footerPanel.add(buttonPane, BorderLayout.EAST);
		contentPanel.add(footerPanel, BorderLayout.SOUTH);

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
		button.addActionListener(_ -> {
			logger.info("Running function.");
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
			
			if(!CreateProjectService.execute(new ProjectDCO(title, description, iconColorId))) {
				new ErrorDialog("Database Error", "Error while creating project");
			}
			dispose();

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
