package app.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;

import app.cmp.CustomIcon;
import app.entities.IconColor;
import app.entities.Project;
import app.entities.Task;
import app.enums.LightColors;
import app.services.GetIconColorOfProjectService;
import app.services.GetProjectsService;
import app.services.GetTasksOfProjectService;

import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ScrollPaneConstants;

public class Main extends JFrame {
	
	private static Connection conn;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField searchTextField;
	private JScrollPane projectsContainer;
	private JPanel headerPanel;
	private JScrollPane infoScrollPane;
	private ButtonGroup tasksButtonGroup = new ButtonGroup();
	
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
		
		headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		showInfoPanel.add(headerPanel, BorderLayout.NORTH);

		infoScrollPane = new JScrollPane();
		infoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		showInfoPanel.add(infoScrollPane);
		
		JPanel taskActionsPanel = new JPanel();
		showInfoPanel.add(taskActionsPanel, BorderLayout.SOUTH);

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
			
			JLabel label = new JLabel(p.project_title());
			
			int projectId = p.project_id();
			
			JPanel ckSet = new JPanel();

			ckSet.putClientProperty("project_title", p.project_title());
			ckSet.putClientProperty("project_id", projectId);
			ckSet.putClientProperty("list_order", p.list_order());
			ckSet.putClientProperty("icon_color_id", p.icon_color_id());
			ckSet.setLayout(new BoxLayout(ckSet, BoxLayout.X_AXIS));
			ckSet.setAlignmentX(LEFT_ALIGNMENT);
			
			IconColor ic = GetIconColorOfProjectService.execute(conn, projectId);
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
			CreateProjectWindow createProjectWindow = new CreateProjectWindow(Main.this ,conn);
			createProjectWindow.addWindowListener(new WindowAdapter() {
				
				@Override
				public void windowClosed(WindowEvent e) {
					listProjects(projectsContainer);
				}
				
			});
		});
	}
	
	private void addProjectEventListener(JPanel panel) {
		
		if(panel != null) {
			panel.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					panel.setBackground(LightColors.PRIMARY_HOVER.getColor());
					displayProjectInfo(panel);
				}
			});
		}
		
	}
	
	private void displayProjectInfo(JPanel component) {
		
		String title = (String)component.getClientProperty("project_title");
		int id = (int)component.getClientProperty("project_id");
		String description = (String)component.getClientProperty("description");
		
		headerPanel.removeAll();
		headerPanel.add(new JLabel(title), BorderLayout.NORTH);
		headerPanel.add(new JLabel(description), BorderLayout.CENTER);
		headerPanel.revalidate();
		headerPanel.repaint();
		
		List<Task> tasks = GetTasksOfProjectService.execute(id, conn);
		ListIterator<Task> i = tasks.listIterator();
				
		JPanel info = new JPanel();
		info.setLayout(new BorderLayout());
		
		JPanel tasksContainer = new JPanel();
		tasksContainer.setLayout(new BorderLayout());
		info.add(tasksContainer, BorderLayout.NORTH);
		
		
		while(i.hasNext()) {
			tasksContainer.add(createTaskContainer(i.next()));
		}
				
		infoScrollPane.setViewportView(tasksContainer);
		infoScrollPane.revalidate();
		infoScrollPane.repaint();
		
	}
	
	private JPanel createTaskContainer(Task task){
		
		JPanel taskPanel = new JPanel();
		taskPanel.setLayout(new BorderLayout());
		JLabel title = new JLabel(task.task_title());
		
		title.putClientProperty("task_id", task.task_id());
		title.putClientProperty("description", task.description());
		title.putClientProperty("status_id", task.status_id());
		title.putClientProperty("priority", task.priority());
		title.putClientProperty("due_date", task.due_date());
		title.putClientProperty("list_order", task.list_order());
		title.putClientProperty("project_id", task.project_id());
		title.putClientProperty("created_at", task.created_at());
		title.putClientProperty("updated_at", task.updated_at());
		title.putClientProperty("completed_at", task.completed_at());
		
		JCheckBox chk = new JCheckBox();
		
		taskPanel.add(chk,BorderLayout.EAST);
		taskPanel.add(title, BorderLayout.WEST);
		
		tasksButtonGroup.add(chk);
		taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
				
		return taskPanel;
	}

}
