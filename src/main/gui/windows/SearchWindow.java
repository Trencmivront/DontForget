package main.gui.windows;

import javax.swing.JDialog;
import javax.swing.JFrame;

import main.gui.Main;
import main.gui.panels.EmptyPanel;
import main.gui.panels.SearchedItemsPanel;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class SearchWindow extends JDialog{

	private static final long serialVersionUID = 1L;
	private JFrame source;
	private JTextField searchTextField;
	private EmptyPanel emptyPanel = new EmptyPanel("Search Everyting");
	private SearchedItemsPanel searchedItemsPanel;
	private boolean itemsListed = false;
	
	public SearchWindow() {
		super(Main.getMain(), "Search", false);
		
		searchedItemsPanel = new SearchedItemsPanel(this);
		source = Main.getMain();
		
		setSize(source.getWidth() / 2, source.getHeight() / 2);
		setResizable(false);
		setUndecorated(true);
		
		JPanel searchPanel = new JPanel();
		getContentPane().add(searchPanel, BorderLayout.NORTH);
		
		searchTextField = new JTextField();
		searchTextField.setToolTipText("text");
		searchPanel.add(searchTextField);
		searchTextField.setColumns(10);
		
		add(emptyPanel, BorderLayout.CENTER);
		
		addSearchTextFieldEventListener();
		
		refresh();
		setVisible(true);
	}
	
	private void addSearchTextFieldEventListener() {
		searchTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String key = searchTextField.getText();
				if((key.isEmpty() || key.isBlank()) && itemsListed) {
					remove(searchedItemsPanel);
					add(emptyPanel, BorderLayout.CENTER);
					itemsListed = false;
				}
				else if(!itemsListed && !key.isBlank() && !key.isEmpty()){
					remove(emptyPanel);
					add(searchedItemsPanel, BorderLayout.CENTER);
					itemsListed = true;
				}
				searchedItemsPanel.filterRows(key);
				refresh();
			}
		});
	}
	
	private void refresh() {
		revalidate();
		repaint();
	}
	
}
