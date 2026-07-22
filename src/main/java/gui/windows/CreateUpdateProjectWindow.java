package main.java.gui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.custom.SpringContext;
import org.springframework.http.ResponseEntity;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.github.lgooddatepicker.zinternaltools.WrapLayout;

import main.java.controllers.ProjectController;
import main.java.controllers.IconColorController;
import main.java.dco.ProjectDCO;
import main.java.entities.IconColor;
import main.java.gui.panels.ProjectRowPanel;
import main.java.gui.popups.ErrorDialog;

public class CreateUpdateProjectWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField projectTitleTextField;
	private JTextArea descriptionTextArea;
	private ButtonGroup bg;
	private static final Logger logger = LoggerFactory.getLogger(CreateUpdateProjectWindow.class.getName());

	private static final int TITLE_MAX_CHARACTER = 50;
	private static final int BODY_MAX_CHARACTER = 500;
	private boolean isUpdate = false;
	private ProjectRowPanel updatedProject;
	private final ProjectController projectController = SpringContext.getBean(ProjectController.class);
	private final IconColorController iconColorController = SpringContext.getBean(IconColorController.class);
	
	/**
	 * Create the dialog.
	 */
	public CreateUpdateProjectWindow(JFrame source, Boolean isUpdate, ProjectRowPanel updatedProject) {
		logger.info("Drawing the window.");
		
		super(source, "Create Project", false);
		this.isUpdate = isUpdate;
		this.updatedProject = updatedProject;
		
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		Dimension size = source.getSize();
		int w = Math.min(480, (int) (size.getWidth() * 0.75));
		int h = Math.min(400, (int) (size.getHeight() * 0.75));
		setSize(new Dimension(w, h));
		setLocationRelativeTo(source);

		// Content Panel with standard margin
		contentPanel.setLayout(new BorderLayout(15, 15));
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// 1. Project Title (Header Panel)
		projectTitleTextField = new JTextField();
		projectTitleTextField.setFont(new Font("Dialog", Font.BOLD, 15));
		projectTitleTextField.putClientProperty("JTextField.placeholderText", "Title of the project");
		projectTitleTextField.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		addTextFieldDocumentFilter(projectTitleTextField);
		
		contentPanel.add(projectTitleTextField, BorderLayout.NORTH);

		// 2. Center Panel (Description + Options/Colors)
		JPanel centerPanel = new JPanel(new BorderLayout(0, 12));

		descriptionTextArea = new JTextArea();
		descriptionTextArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.putClientProperty("JTextArea.placeholderText", "Add details or description...");
		descriptionTextArea.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		
		addTextAreaDocumentFilter(descriptionTextArea);

		JScrollPane descScrollPane = new JScrollPane(descriptionTextArea);
		centerPanel.add(descScrollPane, BorderLayout.CENTER);

		// Color Panel under description (replaces task options Panel)
		JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		colorPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

		JLabel colorLabel = new JLabel("Color: ");
		colorLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		colorPanel.add(colorLabel);

		JPanel colorRadioPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 5, 0));
		colorPanel.add(colorRadioPanel);

		centerPanel.add(colorPanel, BorderLayout.SOUTH);
		contentPanel.add(centerPanel, BorderLayout.CENTER);

		// 3. Footer Panel (Cancel + Create buttons)
		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		JButton okButton = new JButton("OK");
		okButton.setFont(new Font("Dialog", Font.BOLD, 14));
		okButton.putClientProperty("JButton.buttonType", "roundRect");

		buttonPane.add(okButton);
		footerPanel.add(buttonPane, BorderLayout.EAST);
		contentPanel.add(footerPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(okButton);
		
		listColors(colorRadioPanel);
		addOkButtonActionListener(okButton);

		if (isUpdate && (updatedProject != null)) {
			projectTitleTextField.setText((String)updatedProject.getClientProperty("projectTitle"));
			descriptionTextArea.setText((String)updatedProject.getClientProperty("description"));
			setSelectedRadioButton(colorRadioPanel);
		}

		revalidate();
		repaint();
		setVisible(true);

		logger.info("Window is ready.");
	}
	
	private void addOkButtonActionListener(JButton button) {
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
			Long iconColorId = 1L;
			if(selectedRadioButton != null) {
				Object colorIdObj = selectedRadioButton.getClientProperty("iconColorId");
				if (colorIdObj instanceof Number) {
					iconColorId = ((Number) colorIdObj).longValue();
				}
			}
			
			if(isUpdate) {
				Long id = (Long)updatedProject.getClientProperty("projectId");
				try {
					ResponseEntity<String> re = projectController.updateProject(new ProjectDCO(title, description, iconColorId), id);
					if (re.getStatusCode().value() >= 400) {
						new ErrorDialog("Database Error", "Error while updating project");
					}
				} catch (Exception e) {
					e.printStackTrace();
					new ErrorDialog("Database Error", "Error while updating project");
				}
			}
			else {
				try {
					ResponseEntity<Long> re = projectController.createProject(new ProjectDCO(title, description, iconColorId));
					if (re.getStatusCode().value() >= 400) {
						new ErrorDialog("Database Error", "Error while creating project");
					}
				} catch (Exception e) {
					e.printStackTrace();
					new ErrorDialog("Database Error", "Error while creating project");
				}
			}
			dispose();

		});
	}
	
	private void listColors(Container container) {
		logger.info("Running function.");

		List<IconColor> ic = Collections.emptyList();
		try {
			ResponseEntity<List<IconColor>> response = iconColorController.getIconColors();
			ic = response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// initialize the ButtonGroup here
		bg = new ButtonGroup();
		
		if (ic != null) {
			for(IconColor color : ic) {
				JRadioButton rb = new JRadioButton();
				rb.setActionCommand(Long.toString(color.iconColorId()));
				rb.setBackground(new Color(color.red(), color.green(), color.blue()));
				rb.putClientProperty("iconColorId", color.iconColorId());
				bg.add(rb);
				container.add(rb);
			}
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
	
	private void setSelectedRadioButton(JPanel colorRadioPanel) {
		if (updatedProject == null) {
			return;
		}
		Object projIdObj = updatedProject.getClientProperty("projectId");
		if (projIdObj instanceof Number) {
			Long projectId = ((Number) projIdObj).longValue();
			IconColor projectColor = null;
			try {
				ResponseEntity<IconColor> response = iconColorController.getIconColorOfProject(projectId);
				projectColor = response.getBody();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (projectColor != null) {
				for (Component comp : colorRadioPanel.getComponents()) {
					if (comp instanceof JRadioButton) {
						JRadioButton rb = (JRadioButton) comp;
						Object rbColorIdObj = rb.getClientProperty("iconColorId");
						if (rbColorIdObj instanceof Number) {
							Long rbColorId = ((Number) rbColorIdObj).longValue();
							if (rbColorId.equals(projectColor.iconColorId())) {
								rb.setSelected(true);
								bg.setSelected(rb.getModel(), true);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private void addTextFieldDocumentFilter(JTextField field) {
		((AbstractDocument) field.getDocument()).setDocumentFilter(createDocumentFilter(TITLE_MAX_CHARACTER));
	}
	
	private void addTextAreaDocumentFilter(JTextArea area) {
		((AbstractDocument) area.getDocument()).setDocumentFilter(createDocumentFilter(BODY_MAX_CHARACTER));
	}
	
	private DocumentFilter createDocumentFilter(int maxCharacter) {
		return new DocumentFilter() {
//			Here we update the text area same way we update jtable.
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
				if (string == null) {
//					and there is no string to be inserted
					return;
				}
//				Insert the string if it length is smaller than max character length
				if ((fb.getDocument().getLength() + string.length()) <= maxCharacter) {
					super.insertString(fb, offset, string, attr);
				} else {
//					get substring of string if text size is bigger than max character
					int remaining = maxCharacter - fb.getDocument().getLength();
					if (remaining > 0) {
						super.insertString(fb, offset, string.substring(0, remaining), attr);
					}
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (text == null) {
					super.replace(fb, offset, length, null, attrs);
					return;
				}
				int currentLength = fb.getDocument().getLength();
				if ((currentLength - length + text.length()) <= maxCharacter) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					int remaining = maxCharacter - currentLength + length;
					if (remaining > 0) {
						super.replace(fb, offset, length, text.substring(0, remaining), attrs);
					}
				}
			}
		};
	}

}
