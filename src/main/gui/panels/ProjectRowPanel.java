package main.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import main.cmp.CustomIcon;
import main.entities.IconColor;
import main.entities.Project;
import main.gui.Main;
import main.gui.windows.CreateUpdateProjectWindow;
import main.services.icon.GetIconColorOfProjectService;
import main.services.project.DeleteProjectService;

public class ProjectRowPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Main main = Main.getMain();

	public ProjectRowPanel(Project project) {
		this(null, project);
	}
	
	public ProjectRowPanel(JCheckBox ck, Project project) {
		
		JLabel label = new JLabel(project.project_title());
		
		Long projectId = project.project_id();
		
		putClientProperty("project_title", project.project_title());
		putClientProperty("description", project.description());
		putClientProperty("project_id", projectId);
		putClientProperty("list_order", project.list_order());
		putClientProperty("icon_color_id", project.icon_color_id());
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		setBorder(new EmptyBorder(3, 2, 3, 2));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getFont().getSize() + 5));
		
		IconColor ic = GetIconColorOfProjectService.execute(projectId);
		if(ic != null) {
			Color color = new Color(ic.red(), ic.green(), ic.blue());
			label.setIcon(new CustomIcon(color, 12, 12));
		}
		
		if(ck != null) {
			add(ck);
		}

		add(label);
		addPopUpMenuItem();
		addMouseListener();
	}
	
	private void addMouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// removing background color from previous project button
				main.setProjectBackgroundColorOfProject(ProjectRowPanel.this);
				JPanel showInfoPanel = main.getShowInfoPanel();
				showInfoPanel.removeAll();
				showInfoPanel.add(new ProjectInfoPanel(ProjectRowPanel.this));
				main.refreshWindow();
			}
		});
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
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
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
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
			CreateUpdateProjectWindow dialog = new CreateUpdateProjectWindow(frame, true, this);
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent we) {
					refreshProjectsList();
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
				Long projectId = (Long) getClientProperty("project_id");
				if (DeleteProjectService.execute(projectId)) {
					refreshProjectsList();
				} else {
					JOptionPane.showMessageDialog(this, "Failed to delete the project.");
				}
			}
		});
	}

	private void refreshProjectsList() {
		if (main != null && main.getProjectsContainer() != null) {
			try {
				Method method = Main.class.getDeclaredMethod("listProjects", JScrollPane.class);
				method.setAccessible(true);
				method.invoke(main, main.getProjectsContainer());
				main.refreshWindow();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
