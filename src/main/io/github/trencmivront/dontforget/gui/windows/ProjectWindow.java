package main.io.github.trencmivront.dontforget.gui.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import main.io.github.trencmivront.dontforget.controllers.ProjectController;
import main.io.github.trencmivront.dontforget.custom.DocumentFilterFactory;
import main.io.github.trencmivront.dontforget.custom.SpringContext;
import main.io.github.trencmivront.dontforget.dto.IconColorDTO;
import main.io.github.trencmivront.dontforget.dto.ProjectDTO;
import main.io.github.trencmivront.dontforget.gui.Main;
import main.io.github.trencmivront.dontforget.gui.panels.IconColorPanel;
import main.io.github.trencmivront.dontforget.gui.popups.ErrorDialog;

public class ProjectWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField projectTitleTextField;
	private JTextArea descriptionTextArea;
	private static final Logger logger = LoggerFactory.getLogger(ProjectWindow.class.getName());

	private static final int TITLE_MAX_LENGTH = 50;
	private static final int BODY_MAX_LENGTH = 500;
	private boolean isUpdate;
	private ProjectDTO updateProjectDTO;
	private IconColorPanel iconColorPanel;
	private final ProjectController projectController = SpringContext.getBean(ProjectController.class);
	
	private static ProjectWindow projectWindow;
	
	private static final Main main = Main.getMain();
	
	/**
	 * Create the dialog.
	 */
	public ProjectWindow(ProjectDTO projectDTO) {
		logger.info("Drawing the window.");
		
//		only one instance of task window at a time
		if(projectWindow != null) {
			projectWindow.dispose();
			projectWindow = null;
		}
		
		super(main, "Project", false);
		
		projectWindow = this;
		
		if(projectDTO!= null) {
			isUpdate = true;
			this.updateProjectDTO = projectDTO;
		}
		
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		Dimension size = main.getSize();
		int w = Math.min(480, (int) (size.getWidth() * 0.75));
		int h = Math.min(400, (int) (size.getHeight() * 0.75));
		setSize(new Dimension(w, h));

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

		addColorPanel(centerPanel);
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
		
		addOkButtonActionListener(okButton);

		if (isUpdate && (updateProjectDTO != null)) {
			projectTitleTextField.setText(updateProjectDTO.getProjectTitle());
			descriptionTextArea.setText(updateProjectDTO.getDescription());
			iconColorPanel.setSelectedColor(updateProjectDTO.getIconColorId());
		}
		
		int x = (int) getOwner().getLocationOnScreen().getX() + (getOwner().getWidth() / 2 + getWidth());
		int y = (int) getOwner().getLocationOnScreen().getY() + (getOwner().getHeight() / 2 + getHeight());
		setLocation(x, y);
		setVisible(true);

		revalidate();
		repaint();
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
			IconColorDTO selectedIconColorDTO = iconColorPanel.getSelectedColor();
			Long iconColorId = 1L;
			
			if(selectedIconColorDTO != null) {
				iconColorId = selectedIconColorDTO.getIconColorId();
			}
			
			if(isUpdate) {
				Long id = updateProjectDTO.getProjectId();
				ResponseEntity<String> re = projectController.updateProject(new ProjectDTO(id, title, description, iconColorId));
				if (re.getStatusCode().value() >= 400) {
					new ErrorDialog("Database Error", "Error while updating project");
				}
			}
			else {
				ResponseEntity<Long> re = projectController.createProject(new ProjectDTO(null, title, description, iconColorId));
				if (re.getStatusCode().value() >= 400) {
					new ErrorDialog("Database Error", "Error while creating project");
				}
			}
			if(SearchWindow.getSearchWindow() != null) {
				SearchWindow.getSearchWindow().clear();
			}
			dispose();

		});
	}
	
	private void addColorPanel(JPanel panel) {
		logger.info("Running function.");
		iconColorPanel = new IconColorPanel();
		panel.add(iconColorPanel, BorderLayout.SOUTH);
	}
	
	private void addTextFieldDocumentFilter(JTextField field) {
		((AbstractDocument) field.getDocument()).setDocumentFilter(DocumentFilterFactory.getDocumentFilter(TITLE_MAX_LENGTH));
	}
	
	private void addTextAreaDocumentFilter(JTextArea area) {
		((AbstractDocument) area.getDocument()).setDocumentFilter(DocumentFilterFactory.getDocumentFilter(BODY_MAX_LENGTH));
	}
	
}
