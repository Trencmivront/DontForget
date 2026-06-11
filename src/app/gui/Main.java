package app.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField searchTextField;

	/**
	 * Create the frame.
	 */
	public Main() {
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
		
		JPanel rightContainer = new JPanel();
		mainContainer.setRightComponent(rightContainer);

		setVisible(true);
	}

}
