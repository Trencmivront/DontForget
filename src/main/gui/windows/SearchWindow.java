package main.gui.windows;

import javax.swing.JDialog;
import javax.swing.JFrame;

import main.gui.Main;
import main.gui.panels.EmptyPanel;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;

public class SearchWindow extends JDialog{

	private static final long serialVersionUID = 1L;
	private JFrame source;
	private JTextField searchTextField;
	private EmptyPanel emptyPanel = new EmptyPanel("Search Everyting");
	
	public SearchWindow() {
				
		super(Main.main, "Search", true);
		
		source = Main.main;
		
		setSize(source.getWidth() / 2, source.getHeight() / 2);
		setResizable(false);
		
		JPanel searchPanel = new JPanel();
		getContentPane().add(searchPanel, BorderLayout.NORTH);
		
		searchTextField = new JTextField();
		searchTextField.setToolTipText("text");
		searchPanel.add(searchTextField);
		searchTextField.setColumns(10);
		
		JButton searchButton = new JButton("-");
		searchButton.setToolTipText("search");
		searchPanel.add(searchButton);
		
		add(emptyPanel, BorderLayout.CENTER);
		
		refresh();
		setVisible(true);
	}
	
	
	
	private void refresh() {
		revalidate();
		repaint();
	}
	
}
