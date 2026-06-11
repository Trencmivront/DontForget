package app.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import app.entities.Project;
import app.services.GetProjectsService;

import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.sql.Connection;
import java.util.List;

public class Main extends JFrame {
	
	private static Connection conn;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField searchTextField;
	
	/**
	 * Create the frame.
	 */
	public Main(Connection conn) {
		Main.conn = conn;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		
		searchTextField = new JTextField();
		leftTopContainer.add(searchTextField);
		searchTextField.setColumns(10);
		
		JButton searchButton = new JButton("Search");
		leftTopContainer.add(searchButton);
		
		JButton hideLeftPanelButton = new JButton("hide");
		leftTopContainer.add(hideLeftPanelButton);
		
		JPanel leftBottomContainer = new JPanel();
		leftContainer.add(leftBottomContainer, BorderLayout.CENTER);
		leftBottomContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel newProjectField = new JPanel();
		leftBottomContainer.add(newProjectField, BorderLayout.NORTH);
		
		JLabel newProjectLabel = new JLabel("new project");
		newProjectField.add(newProjectLabel);
		
		JButton newProjectButton = new JButton("+");
		newProjectField.add(newProjectButton);
		
		JScrollPane projectsContainer = new JScrollPane();
		leftBottomContainer.add(projectsContainer, BorderLayout.CENTER);
		listProjects(projectsContainer);
		
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
		
		JPanel showInfoPanel = new JPanel();
		rightContainer.add(showInfoPanel, BorderLayout.CENTER);
		showInfoPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel headerPanel = new JPanel();
		showInfoPanel.add(headerPanel, BorderLayout.NORTH);
		
		JScrollPane contentPanel = new JScrollPane();
		showInfoPanel.add(contentPanel);

		setVisible(true);
	}
	
	private void listProjects(JScrollPane container) {
		List<Project> projects = GetProjectsService.execute(conn);
		int size = projects.size();
		
		if(size <= 0) {
			return;
		}
				
		JPanel ckContainer = new JPanel();
		ckContainer.setLayout(new BoxLayout(ckContainer, BoxLayout.Y_AXIS));
		
		for(int i = 0; i < size; i++) {
			Project p = projects.get(i);
			JCheckBox ck =  new JCheckBox();
			ck.putClientProperty("project_id", p.getProject_id());
			ck.putClientProperty("list_order", p.getList_order());
			ck.putClientProperty("icon_color_id", p.getIcon_color_id());
			
			JLabel label = new JLabel(p.getProject_title());
			
			JPanel ckSet = new JPanel();
			ckSet.setLayout(new BoxLayout(ckSet, BoxLayout.X_AXIS));
			ckSet.setAlignmentX(LEFT_ALIGNMENT);
			
			ckSet.add(ck);
			ckSet.add(label);
			ckContainer.add(ckSet);
		}
		// we put it in a viewport
		// that's how components are displayed in a JScrollPane
		JViewport viewport = new JViewport();
		viewport.add(ckContainer);
		
		container.setViewport(viewport);
	}
	
	private void addProjectEventListener() {
		
	}

}
