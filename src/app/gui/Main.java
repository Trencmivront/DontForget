package app.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;

import app.cmp.CustomIcon;
import app.entities.IconColor;
import app.entities.Project;
import app.enums.LightColors;
import app.services.GetIconColorOfProjectService;
import app.services.GetProjectsService;

public class Main extends JFrame {
	private static JFrame mainFrame;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField searchTextField;
	private JScrollPane projectsContainer;
	private JPanel showInfoPanel;
	private JPanel prevProjectPanel;
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	/**
	 * Create the frame.
	 */
	public Main() {
		logger.info("Drawing Main window.");
		setFont(new Font("Times New Roman", Font.PLAIN, 20));
		mainFrame = Main.this;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
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
		leftTopContainer.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel placeholderLabel = new JLabel("");
		leftTopContainer.add(placeholderLabel);
		
		JButton hideLeftPanelButton = new JButton("hide");
		hideLeftPanelButton.setFont(new Font("Dialog", Font.BOLD, 20));
		leftTopContainer.add(hideLeftPanelButton);
		
		searchTextField = new JTextField();
		searchTextField.setFont(new Font("Dialog", Font.PLAIN, 20));
		leftTopContainer.add(searchTextField);
		searchTextField.setColumns(10);
		
		JButton searchButton = new JButton("Search");
		searchButton.setFont(new Font("Dialog", Font.BOLD, 20));
		leftTopContainer.add(searchButton);
		
		JPanel leftBottomContainer = new JPanel();
		leftContainer.add(leftBottomContainer, BorderLayout.CENTER);
		leftBottomContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel newProjectField = new JPanel();
		leftBottomContainer.add(newProjectField, BorderLayout.NORTH);
		
		JLabel newProjectLabel = new JLabel("new project");
		newProjectLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		newProjectField.add(newProjectLabel);
		
		JButton newProjectButton = new JButton("+");
		newProjectButton.setFont(new Font("Dialog", Font.BOLD, 20));
		newProjectField.add(newProjectButton);
		addCreateProjectEventListener(newProjectButton);
		
		projectsContainer = new JScrollPane();
		leftBottomContainer.add(projectsContainer, BorderLayout.CENTER);
		listProjects(projectsContainer);
		
		JPanel rightContainer = new JPanel();
		mainContainer.setRightComponent(rightContainer);
		rightContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel buttonMenuPanel = new JPanel();
		rightContainer.add(buttonMenuPanel, BorderLayout.NORTH);
		buttonMenuPanel.setLayout(new BoxLayout(buttonMenuPanel, BoxLayout.X_AXIS));
		
		JButton tagsButton = new JButton("tags");
		tagsButton.setFont(new Font("Dialog", Font.BOLD, 20));
		buttonMenuPanel.add(tagsButton);
		
		JButton inboxButton = new JButton("inbox");
		inboxButton.setFont(new Font("Dialog", Font.BOLD, 20));
		buttonMenuPanel.add(inboxButton);
		addInboxButtonEventListener(inboxButton);
		
		JButton todayButton = new JButton("today");
		todayButton.setFont(new Font("Dialog", Font.BOLD, 20));
		buttonMenuPanel.add(todayButton);
		
		JButton remindersButton = new JButton("reminders");
		remindersButton.setFont(new Font("Dialog", Font.BOLD, 20));
		buttonMenuPanel.add(remindersButton);
		addReminderActionListener(remindersButton);
		
		showInfoPanel = new JPanel();
		rightContainer.add(showInfoPanel, BorderLayout.CENTER);
		showInfoPanel.setLayout(new BorderLayout(0, 0));
		
		refreshWindow();
		
		addWindowListener(new WindowAdapter() {
			
			
		});
		
		setVisible(true);
		logger.info("Main window is ready.");
	}
	
	private void listProjects(JScrollPane container) {
		List<Project> projects = null;
		container.removeAll();
		try {
			projects = GetProjectsService.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JDialog(), e.getMessage());
			return;
		}
		
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
	
	private void addCreateProjectEventListener(JButton button) {
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
	
	private void addInboxButtonEventListener(JButton button) {
		button.addActionListener(_ ->{
			showInfoPanel.removeAll();
			showInfoPanel.add(new InboxPanel());
			refreshWindow();
		});
	}
	
	private void addProjectEventListener(JPanel panel) {
		
		if(panel != null) {
			panel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(prevProjectPanel != null) prevProjectPanel.setBackground(null);
					panel.setBackground(LightColors.PRIMARY_HOVER.getColor());
					showInfoPanel.removeAll();
					showInfoPanel.add(new ProjectInfoPanel(panel));
					prevProjectPanel = panel;
					refreshWindow();
				}
			});
		}
	}
	
	private void addReminderActionListener(JButton button) {
		button.addActionListener(_->{
			showInfoPanel.removeAll();
			showInfoPanel.add(new ReminderPanel());
			refreshWindow();
		});
	}
	
	private void refreshWindow() {
		mainFrame.revalidate();
		mainFrame.repaint();
	}
}
