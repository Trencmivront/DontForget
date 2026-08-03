package main.java.gui.panels.rows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import main.java.custom.SpringContext;
import main.java.controllers.IconColorController;
import main.java.controllers.ProjectController;
import org.springframework.http.ResponseEntity;

import main.java.custom.CustomIcon;
import main.java.dto.IconColorDTO;
import main.java.dto.ProjectDTO;
import main.java.gui.Main;
import main.java.gui.panels.ProjectInfoPanel;
import main.java.gui.windows.ProjectWindow;

public class ProjectRowPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(ProjectRowPanel.class.getName());

	private static final long serialVersionUID = 1L;
	
	private static final Main main = Main.getMain();

	private IconColorController iconColorController;
	private ProjectController projectController;

	private ProjectDTO projectDTO;
	
	public ProjectDTO getProjectDTO() {
		return projectDTO;
	}
	
	public ProjectRowPanel(ProjectDTO project) {
		logger.info("Initializing ProjectRowPanel");
		this(null, project);
	}
	
	public ProjectRowPanel(JCheckBox ck, ProjectDTO project) {
		this.iconColorController = SpringContext.getBean(IconColorController.class);
		this.projectController = SpringContext.getBean(ProjectController.class);
		
		projectDTO = project;
		
		JLabel label = new JLabel(projectDTO.getProjectTitle());
		
		Long projectId = projectDTO.getProjectId();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		setBorder(new EmptyBorder(3, 2, 3, 2));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getFont().getSize() + 5));
		
		IconColorDTO ic = null;
		try {
			ResponseEntity<IconColorDTO> response = iconColorController.getIconColorOfProject(projectId);
			ic = response.getBody();
		} catch (Exception e) {
			logger.error("Failed to fetch icon color for project " + projectId, e);
		}

		if(ic != null) {
			Color color = new Color(ic.getRed(), ic.getGreen(), ic.getBlue());
			label.setIcon(new CustomIcon(color, 12, 12));
		}
		
		if(ck != null) {
			add(ck);
		}

		add(label);
		addPopUpMenuItem();
	}
	
	private void addPopUpMenuItem() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem editItem = new JMenuItem("Edit");
		JMenuItem deleteItem = new JMenuItem("Delete");
		
		popupMenu.add(editItem);
		popupMenu.add(deleteItem);
		
		addEditActionListener(editItem);
		addDeleteActionListener(deleteItem);
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// removing background color from previous project button
				if(e.getButton() == MouseEvent.BUTTON1) {
					main.setProjectBackgroundColorOfProject(ProjectRowPanel.this);
					JPanel showInfoPanel = main.getShowInfoPanel();
					showInfoPanel.removeAll();
					showInfoPanel.add(new ProjectInfoPanel(projectDTO));
					main.refreshWindow();
				}
				else if(e.getButton() == MouseEvent.BUTTON3) {
					showPopup(e);
				}
			}
			private void showPopup(MouseEvent e) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		};
		
		addMouseListener(mouseAdapter);
	}

	private void addEditActionListener(JMenuItem button) {
		button.addActionListener(_ -> {
			ProjectWindow dialog = new ProjectWindow(projectDTO);
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent we) {
					main.listProjects(main.getProjectsContainer());
				}
			});
		});
	}

	private void addDeleteActionListener(JMenuItem button) {
		button.addActionListener(_ -> {
			int confirm = JOptionPane.showConfirmDialog(
				this,
				"Are you sure you want to delete this project?",
				"Confirm Delete",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
			);
			if (confirm == JOptionPane.YES_OPTION) {
				Long projectId = projectDTO.getProjectId();
				int code = 500;
				try {
					ResponseEntity<String> response = projectController.deleteProject(projectId);
					code = response.getStatusCode().value();
				} catch (Exception e) {
					logger.error("Failed to delete project " + projectId, e);
				}
				if (code < 400) {
					main.listProjects(main.getProjectsContainer());
				} else {
					JOptionPane.showMessageDialog(this, "Failed to delete the project.");
				}
			}
		});
	}
}
