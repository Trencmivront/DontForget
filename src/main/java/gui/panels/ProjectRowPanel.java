package main.java.gui.panels;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.api.Api;
import main.java.custom.CustomIcon;
import main.java.entities.IconColor;
import main.java.entities.Project;
import main.java.gui.Main;
import main.java.gui.windows.CreateUpdateProjectWindow;

public class ProjectRowPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(ProjectRowPanel.class.getName());

	private static final long serialVersionUID = 1L;
	
	private static final Main main = Main.getMain();
	private final Api api = new Api();
	private final ObjectMapper mapper = new ObjectMapper();

	public ProjectRowPanel(Project project) {
		logger.info("Initializing ProjectRowPanel");
		this(null, project);
	}
	
	public ProjectRowPanel(JCheckBox ck, Project project) {
		
		JLabel label = new JLabel(project.projectTitle());
		
		Long projectId = project.projectId();
		
		putClientProperty("projectTitle", project.projectTitle());
		putClientProperty("description", project.description());
		putClientProperty("projectId", projectId);
		putClientProperty("listOrder", project.listOrder());
		putClientProperty("iconColorId", project.iconColorId());
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		setBorder(new EmptyBorder(3, 2, 3, 2));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getFont().getSize() + 5));
		
		IconColor ic = null;
		try {
			String res = api.get("/api/icon-color/project/", projectId);
			ic = mapper.readValue(res, IconColor.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ic != null) {
			Color color = new Color(ic.red(), ic.green(), ic.blue());
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
					showInfoPanel.add(new ProjectInfoPanel(ProjectRowPanel.this));
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
			CreateUpdateProjectWindow dialog = new CreateUpdateProjectWindow(main, true, this);
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
				Long projectId = (Long) getClientProperty("projectId");
				int code = api.delete("/api/project/delete/", projectId);
				if (code < 400) {
					main.listProjects(main.getProjectsContainer());
				} else {
					JOptionPane.showMessageDialog(this, "Failed to delete the project.");
				}
			}
		});
	}
}
