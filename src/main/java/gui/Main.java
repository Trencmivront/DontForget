package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.custom.SpringContext;
import main.java.controllers.ProjectController;
import org.springframework.http.ResponseEntity;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import main.java.dto.ProjectDTO;
import main.java.gui.panels.InboxPanel;
import main.java.gui.panels.ReminderPanel;
import main.java.gui.panels.TagsPanel;
import main.java.gui.panels.TodayPanel;
import main.java.gui.panels.rows.ProjectRowPanel;
import main.java.gui.windows.ProjectWindow;
import main.java.gui.windows.SearchWindow;

public class Main extends JFrame {
	private static final int WINDOW_HEIGHT= 500;
	private static final int WINDOW_WIDTH = 750;
	private static final int WINDOW_X = 100;
	private static final int WINDOW_Y = 100;
	
	private static final long serialVersionUID = 1L;
	private JSplitPane mainContainer;
	private JScrollPane projectsContainer;
	private JPanel contentPane;
	private JPanel showInfoPanel;
	private JPanel prevProjectPanel;
	private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());
	private static Main main;
	private List<ProjectRowPanel> selectedProjects = new ArrayList<>();
	
	private JButton deleteProjectsButton;
	private JButton tagsButton;
	private JButton inboxButton;
	private JButton todayButton;
	private JButton remindersButton;
	
	private final ProjectController projectController = SpringContext.getBean(ProjectController.class);
	
	{
		main = this;
	}
	
	public JScrollPane getProjectsContainer(){
		return projectsContainer;
	}
	
	public JButton getDeleteProjectsButton() {
		return deleteProjectsButton;
	}
	
	public static Main getMain() {
		return main;
	}
	
	public List<ProjectRowPanel> getSelectedProjects(){
		return selectedProjects;
	}
	public JPanel getShowInfoPanel() {
		return showInfoPanel;
	}
	public JButton getTagsButton() {
		return tagsButton;
	}
	public JButton getInboxButton() {
		return inboxButton;
	}
	public JButton getTodayButton() {
		return todayButton;
	}
	public JButton getRemindersButton() {
		return remindersButton;
	}
	
	public Main() {
		
		logger.info("Drawing Main window.");
//		setting the global toucher for main, freaky
		
		setTitle("DontForget");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);

		try {
			setIconImage(ImageIO.read(new File("src/main/resources/dontforget.png")));
		} catch (IOException e) {
			logger.warn("Could not set icon of main window: {}", e.getMessage());
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		mainContainer = new JSplitPane();
		contentPane.add(mainContainer, BorderLayout.CENTER);
		
		JPanel leftContainer = new JPanel();
		mainContainer.setLeftComponent(leftContainer);
		leftContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel leftTopContainer = new JPanel();
		leftContainer.add(leftTopContainer, BorderLayout.NORTH);
		leftTopContainer.setLayout(new BorderLayout(0, 0));
		leftTopContainer.setBorder(new EmptyBorder(5, 1, 30, 1));
		
		JButton searchButton = new JButton(new FlatSearchIcon());
		searchButton.setToolTipText("Search");
		leftTopContainer.add(searchButton, BorderLayout.WEST);
		
		JLabel appNameLabel = new JLabel("DontForget");
		appNameLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		leftTopContainer.add(appNameLabel, BorderLayout.CENTER);
		
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
		newProjectButton.putClientProperty("JButton.buttonType", "roundRect");
		newProjectField.add(newProjectButton, BorderLayout.EAST);
		addCreateProjectActionListener(newProjectButton);
		
		projectsContainer = new JScrollPane();
		leftBottomContainer.add(projectsContainer, BorderLayout.CENTER);
		
		JPanel rightContainer = new JPanel();
		mainContainer.setRightComponent(rightContainer);
		rightContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel buttonMenuPanel = new JPanel();
		buttonMenuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		
		tagsButton = new JButton("Tags");
		buttonMenuPanel.add(tagsButton);
		
		inboxButton = new JButton("Inbox");
		buttonMenuPanel.add(inboxButton);
		
		todayButton = new JButton("Today");
		buttonMenuPanel.add(todayButton);
		
		remindersButton = new JButton("Reminders");
		buttonMenuPanel.add(remindersButton);
		
		JPanel navigationPanel = new JPanel();
		navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
		navigationPanel.add(new JSeparator());
		navigationPanel.add(buttonMenuPanel);
		navigationPanel.add(new JSeparator());
		rightContainer.add(navigationPanel, BorderLayout.NORTH);

		showInfoPanel = new JPanel();
		rightContainer.add(showInfoPanel, BorderLayout.CENTER);
		showInfoPanel.setLayout(new BorderLayout(0, 0));
		
//		add action listeners for buttons
		addNavigationButtonActionListener(remindersButton, ReminderPanel.class);
		addNavigationButtonActionListener(inboxButton, InboxPanel.class);
		addNavigationButtonActionListener(tagsButton, TagsPanel.class);
		addNavigationButtonActionListener(todayButton, TodayPanel.class);
		addSearchButtonActionListener(searchButton);
		
		setSplitDivider();
		addWindowFocusListener();
		
		listProjects(projectsContainer);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu optionsMenu = new JMenu("Options");
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(_->System.exit(0));
		
		optionsMenu.add(exitItem);
		menuBar.add(optionsMenu);
		
		toolBar.add(menuBar);

		refreshWindow();
		setLocationRelativeTo(null);
		setVisible(true);
		logger.info("Main window is ready.");
	}
	
	public void listProjects(JScrollPane container) {
		container.removeAll();
		List<ProjectDTO> projects = Collections.emptyList();
		
		try {
			ResponseEntity<List<ProjectDTO>> response = projectController.getProjects();
			projects = response.getBody();
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		if(projects == null) {
			return;
		}
		
		int size = projects.size();
		
		if(size <= 0) {
			return;
		}
				
		JPanel ckContainer = new JPanel();
		ckContainer.setLayout(new BoxLayout(ckContainer, BoxLayout.Y_AXIS));
		
		for(int i = 0; i < size; i++) {
			JCheckBox ck = new JCheckBox();
			JPanel panel = new ProjectRowPanel(ck, projects.get(i));
			addCheckBoxEventListener(ck);
			ckContainer.add(panel);
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
			ProjectRowPanel panel = (ProjectRowPanel)ck.getParent();
			if (panel == null) {
				return;
			}
			if (ck.isSelected()) {
				if (!Main.getMain().getSelectedProjects().contains(panel)) {
					this.getSelectedProjects().add(panel);
				}
			} else {
				Main.getMain().getSelectedProjects().remove(panel);
			}

			if (selectedProjects != null) {
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
				for (ProjectRowPanel panel : selectedProjects) {
					Long projectId = panel.getProjectDTO().getProjectId();
					if (projectId != null) {
						try {
							projectController.deleteProject(projectId);
						} catch (Exception e) {
							logger.error("Failed to delete project " + projectId, e);
						}
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
//			Here we want to create the project
			ProjectWindow projectWindow = new ProjectWindow(null);
			projectWindow.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					listProjects(projectsContainer);
				}
			});
		});
	}
	
	private <T> void addNavigationButtonActionListener(JButton button, Class<T> panelClass) {
		button.addActionListener(_ ->{
			setProjectBackgroundColorOfProject(null);
			showInfoPanel.removeAll();
			try {
				showInfoPanel.add((JPanel)panelClass.getConstructor().newInstance());
			}catch (Exception e) {
				e.printStackTrace();
			}
			refreshWindow();
		});
	}
	
	private void addSearchButtonActionListener(JButton button) {
		button.addActionListener(_->new SearchWindow());
	}
	
	private void setSplitDivider() {
		BasicSplitPaneUI splitUi = (BasicSplitPaneUI)mainContainer.getUI();
		BasicSplitPaneDivider divider = splitUi.getDivider();
		divider.setBorder(null);
		mainContainer.putClientProperty("FlatLaf.style", "background: #000000");
		mainContainer.setDividerSize(14);
		divider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					mainContainer.setDividerLocation(0);
				}
			}
		});
	}

	private void addWindowFocusListener() {
		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				return;
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				for(Window w : getOwnedWindows()) {
					w.dispose();
				}
			}
		});
	}
	
	public void destroyChildWindows() {
		requestFocus();
	}
	
	public void destroyChildWindowsExcluding(Window exclude) {
		for(Window w : getOwnedWindows()) {
			if(w != exclude) {
				w.dispose();
			}
		}
	}
	
	public void refreshWindow() {
		revalidate();
		repaint();
	}
	
	public void setProjectBackgroundColorOfProject(JPanel project) {
		if(prevProjectPanel != null) prevProjectPanel.setBackground(null);
		if(project != null) project.setBackground(Color.cyan);
		prevProjectPanel = project;
	}

}
