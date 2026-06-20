package main.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.WindowConstants;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.icons.FlatSearchIcon;

import main.cmp.CustomIcon;
import main.entities.IconColor;
import main.entities.Project;
import main.enums.LightColors;
import main.gui.panels.InboxPanel;
import main.gui.panels.ProjectInfoPanel;
import main.gui.panels.ReminderPanel;
import main.gui.panels.TagPanel;
import main.gui.panels.TodayPanel;
import main.gui.windows.CreateProjectWindow;
import main.services.icon.GetIconColorOfProjectService;
import main.services.project.DeleteProjectService;
import main.services.project.GetProjectsService;

public class Main extends JFrame {	
	private static final int WINDOW_HEIGHT= 500;
	private static final int WINDOW_WIDTH = 750;
	private static final int WINDOW_X = 100;
	private static final int WINDOW_Y = 100;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane projectsContainer;
	private JPanel showInfoPanel;
	private JPanel prevProjectPanel;
	private JButton deleteProjectsButton;
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	public static Main main;
	private List<JPanel> selectedProjects = new ArrayList<>();
	/**
	 * Create the frame.
	 */
	public Main() {
		logger.info("Drawing Main window.");
//		setting the global toucher for main, freaky
		main =  Main.this;
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane mainContainer = new JSplitPane();
		contentPane.add(mainContainer, BorderLayout.CENTER);
		
		JPanel leftContainer = new JPanel();
		mainContainer.setLeftComponent(leftContainer);
		leftContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel leftTopContainer = new JPanel();
		leftContainer.add(leftTopContainer, BorderLayout.NORTH);
		leftTopContainer.setLayout(new BorderLayout(0, 0));
		leftTopContainer.setBorder(new EmptyBorder(5, 1, 30, 1));
		
		JButton searchButton = new JButton(new FlatSearchIcon());
		leftTopContainer.add(searchButton, BorderLayout.WEST);
		
		JLabel appNameLabel = new JLabel("DontForget");
		appNameLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		leftTopContainer.add(appNameLabel, BorderLayout.CENTER);
		
		JButton hideLeftPanelButton = new JButton("");
		leftTopContainer.add(hideLeftPanelButton, BorderLayout.EAST);
		
		JPanel leftBottomContainer = new JPanel();
		leftContainer.add(leftBottomContainer, BorderLayout.CENTER);
		leftBottomContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel newProjectField = new JPanel();
		leftBottomContainer.add(newProjectField, BorderLayout.NORTH);
		newProjectField.setLayout(new BorderLayout(0, 0));
		
		deleteProjectsButton = new JButton("-");
		deleteProjectsButton.setEnabled(false);
		deleteProjectsButton.setVisible(false);
		deleteProjectsButton.setToolTipText("delete-projects");
		newProjectField.add(deleteProjectsButton, BorderLayout.WEST);
		addDeleteProjectActionListener(deleteProjectsButton);
		
		JButton newProjectButton = new JButton("+");
		newProjectField.add(newProjectButton, BorderLayout.EAST);
		addCreateProjectActionListener(newProjectButton);
		
		projectsContainer = new JScrollPane();
		leftBottomContainer.add(projectsContainer, BorderLayout.CENTER);
		
		JPanel rightContainer = new JPanel();
		mainContainer.setRightComponent(rightContainer);
		rightContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel buttonMenuPanel = new JPanel();
		rightContainer.add(buttonMenuPanel, BorderLayout.NORTH);
		buttonMenuPanel.setLayout(new BoxLayout(buttonMenuPanel, BoxLayout.X_AXIS));
		
		JButton tagsButton = new JButton("tags");
		buttonMenuPanel.add(tagsButton);
		
		JButton inboxButton = new JButton("inbox");
		buttonMenuPanel.add(inboxButton);
		
		JButton todayButton = new JButton("today");
		buttonMenuPanel.add(todayButton);
		
		JButton remindersButton = new JButton("reminders");
		buttonMenuPanel.add(remindersButton);
		
		showInfoPanel = new JPanel();
		rightContainer.add(showInfoPanel, BorderLayout.CENTER);
		showInfoPanel.setLayout(new BorderLayout(0, 0));
		
//		add action listeners for buttons
		addNavigationButtonActionListener(remindersButton, ReminderPanel.class.getName());
		addNavigationButtonActionListener(inboxButton, InboxPanel.class.getName());
		addNavigationButtonActionListener(tagsButton, TagPanel.class.getName());
		addNavigationButtonActionListener(todayButton, TodayPanel.class.getName());
		
		listProjects(projectsContainer);

		refreshWindow();
		
		setVisible(true);
		logger.info("Main window is ready.");
	}
	
	private void listProjects(JScrollPane container) {
		List<Project> projects = null;
		container.removeAll();

		projects = GetProjectsService.execute();
		
		int size = projects.size();
		
		if(size <= 0) {
			return;
		}
				
		JPanel ckContainer = new JPanel();
		ckContainer.setLayout(new BoxLayout(ckContainer, BoxLayout.Y_AXIS));
		
		for(int i = 0; i < size; i++) {
			
			Project p = projects.get(i);
			JCheckBox ck =  new JCheckBox();
			
			JLabel label = new JLabel(p.project_title());
			
			int projectId = p.project_id();
			
			JPanel ckSet = new JPanel();

			ckSet.putClientProperty("project_title", p.project_title());
			ckSet.putClientProperty("description", p.description());
			ckSet.putClientProperty("project_id", projectId);
			ckSet.putClientProperty("list_order", p.list_order());
			ckSet.putClientProperty("icon_color_id", p.icon_color_id());
			
			ckSet.setLayout(new BoxLayout(ckSet, BoxLayout.X_AXIS));
			ckSet.setAlignmentX(LEFT_ALIGNMENT);
			
			IconColor ic = GetIconColorOfProjectService.execute(projectId);
			if(ic != null) {
				Color color = new Color(ic.red(), ic.green(), ic.blue());
				label.setIcon(new CustomIcon(color, 12, 12));
			}
			
			ckSet.add(ck);
			ckSet.add(label);
			
			addCheckBoxEventListener(ck);
			addProjectEventListener(ckSet);
							
			ckContainer.add(ckSet);
		}
		// we put it in a viewport
		// that's how components are displayed in a JScrollPane
		JViewport viewport = new JViewport();
		viewport.setFont(new Font("Dialog", Font.PLAIN, 20));
		viewport.add(ckContainer);
		
		container.setViewport(viewport);
	}
	
	private void addCheckBoxEventListener(JCheckBox ck) {
		ck.addActionListener(_->{
			JPanel panel = (JPanel) ck.getParent();
			if (panel == null) {
				return;
			}
			if (ck.isSelected()) {
				if (!selectedProjects.contains(panel)) {
					selectedProjects.add(panel);
				}
			} else {
				selectedProjects.remove(panel);
			}

			if (deleteProjectsButton != null) {
				if (!selectedProjects.isEmpty()) {
					deleteProjectsButton.setEnabled(true);
					deleteProjectsButton.setVisible(true);
				} else {
					deleteProjectsButton.setEnabled(false);
					deleteProjectsButton.setVisible(false);
				}
			}
			refreshWindow();
		});
	}
	
	private void addDeleteProjectActionListener(JButton button) {
		button.addActionListener(_->{
			if (selectedProjects.isEmpty()) {
				return;
			}

			int confirm = JOptionPane.showConfirmDialog(
				Main.this,
				"Are you sure you want to delete the selected projects?",
				"Confirm Delete",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
			);

			if (confirm == JOptionPane.YES_OPTION) {
				for (JPanel panel : selectedProjects) {
					Integer projectId = (Integer) panel.getClientProperty("project_id");
					if (projectId != null) {
						DeleteProjectService.execute(projectId);
					}
				}
				selectedProjects.clear();
				button.setEnabled(false);
				button.setVisible(false);
				listProjects(projectsContainer);
				refreshWindow();
			}
		});
	}
	
	private void addCreateProjectActionListener(JButton button) {
		button.addActionListener(_ -> {
			CreateProjectWindow createProjectWindow = new CreateProjectWindow(Main.this);
			createProjectWindow.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					listProjects(projectsContainer);
				}
				
			});
		});
	}
	
	private void addProjectEventListener(JPanel panel) {
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// removing background color from previous project button
				setProjectBackgroundColor(panel);
				showInfoPanel.removeAll();
				showInfoPanel.add(new ProjectInfoPanel(panel));
				refreshWindow();
			}
		});
	}
	
	private void addNavigationButtonActionListener(JButton button, String className) {
		button.addActionListener(_ ->{
			setProjectBackgroundColor(null);
			showInfoPanel.removeAll();
			try {
				showInfoPanel.add((JPanel)Class.forName(className).getDeclaredConstructor().newInstance());
			}catch (Exception e) {
				e.printStackTrace();
			}
			refreshWindow();
		});
	}
	
	public static void refreshWindow() {
		main.revalidate();
		main.repaint();
	}
	
	private void setProjectBackgroundColor(JPanel project) {
		if(prevProjectPanel != null) prevProjectPanel.setBackground(null);
		if(project != null) project.setBackground(LightColors.PRIMARY_HOVER.getColor());
		prevProjectPanel = project;
	}
}
