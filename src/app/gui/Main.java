package app.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import app.cmp.CustomIcon;
import app.entities.IconColor;
import app.entities.Project;
import app.services.GetIconColorOfProjectService;
import app.services.GetProjectsService;

import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JViewport;

import javax.swing.BoxLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.List;
import javax.swing.ScrollPaneConstants;

public class Main extends JFrame {
	
	private static Connection conn;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField searchTextField;
	private JScrollPane projectsContainer;
	
	/**
	 * Create the frame.
	 */
	public Main(Connection conn) {
		setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
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
		searchTextField.setFont(new Font("Dialog", Font.PLAIN, 20));
		leftTopContainer.add(searchTextField);
		searchTextField.setColumns(10);
		
		JButton searchButton = new JButton("Search");
		searchButton.setFont(new Font("Dialog", Font.BOLD, 20));
		leftTopContainer.add(searchButton);
		
		JButton hideLeftPanelButton = new JButton("hide");
		hideLeftPanelButton.setFont(new Font("Dialog", Font.BOLD, 20));
		leftTopContainer.add(hideLeftPanelButton);
		
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
		
		JButton todayButton = new JButton("today");
		todayButton.setFont(new Font("Dialog", Font.BOLD, 20));
		buttonMenuPanel.add(todayButton);
		
		JButton remindersButton = new JButton("reminders");
		remindersButton.setFont(new Font("Dialog", Font.BOLD, 20));
		buttonMenuPanel.add(remindersButton);
		
		JPanel showInfoPanel = new JPanel();
		rightContainer.add(showInfoPanel, BorderLayout.CENTER);
		showInfoPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel headerPanel = new JPanel();
		showInfoPanel.add(headerPanel, BorderLayout.NORTH);
		
		JScrollPane contentPanel = new JScrollPane();
		contentPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		showInfoPanel.add(contentPanel);

		setVisible(true);
	}
	
	private void listProjects(JScrollPane container) {
		List<Project> projects = null;
		container.removeAll();
		try {
			projects = GetProjectsService.execute(conn);
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
			ck.putClientProperty("project_id", p.project_id());
			ck.putClientProperty("list_order", p.list_order());
			ck.putClientProperty("icon_color_id", p.icon_color_id());
			
			JLabel label = new JLabel(p.project_title());
			
			JPanel ckSet = new JPanel();
			ckSet.setLayout(new BoxLayout(ckSet, BoxLayout.X_AXIS));
			ckSet.setAlignmentX(LEFT_ALIGNMENT);
			
			IconColor ic = GetIconColorOfProjectService.execute(conn, p.project_id());
			if(ic != null) {
				Color color = new Color(ic.red(), ic.green(), ic.blue());
				label.setIcon(new CustomIcon(color, 12, 12));
			}
			
			ckSet.add(ck);
			ckSet.add(label);
							
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
			CreateProjectWindow createProjectWindow = new CreateProjectWindow(conn);
			createProjectWindow.addWindowListener(new WindowAdapter() {
				
				@Override
				public void windowClosed(WindowEvent e) {
					listProjects(projectsContainer);
				}
				
			});
		});
	}

}
