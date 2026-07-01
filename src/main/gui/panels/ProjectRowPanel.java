package main.gui.panels;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.cmp.CustomIcon;
import main.entities.IconColor;
import main.entities.Project;
import main.services.icon.GetIconColorOfProjectService;

public class ProjectRowPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public ProjectRowPanel(JCheckBox ck, Project project) {
		
		JLabel label = new JLabel(project.project_title());
		
		int projectId = project.project_id();
		
		putClientProperty("project_title", project.project_title());
		putClientProperty("description", project.description());
		putClientProperty("project_id", projectId);
		putClientProperty("list_order", project.list_order());
		putClientProperty("icon_color_id", project.icon_color_id());
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		
		IconColor ic = GetIconColorOfProjectService.execute(projectId);
		if(ic != null) {
			Color color = new Color(ic.red(), ic.green(), ic.blue());
			label.setIcon(new CustomIcon(color, 12, 12));
		}
		
		add(ck);
		add(label);
		
	}
	
}
