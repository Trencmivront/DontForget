package main.io.github.trencmivront.dontforget.gui.windows;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.io.github.trencmivront.dontforget.gui.Main;
import main.io.github.trencmivront.dontforget.gui.panels.EmptyPanel;
import main.io.github.trencmivront.dontforget.gui.panels.SearchedItemsPanel;

public class SearchWindow extends JDialog{

	private static final Logger logger = LoggerFactory.getLogger(SearchWindow.class.getName());

	private static final long serialVersionUID = 1L;
	private JFrame source;
	private JTextField searchTextField;
	private EmptyPanel emptyPanel = new EmptyPanel("Search Everyting");
	private SearchedItemsPanel searchedItemsPanel;
	private boolean itemsListed = false;
	
	private static SearchWindow searchWindow;
	
	public static SearchWindow getSearchWindow() {
		return searchWindow;
	}
	
	public SearchWindow() {
		logger.info("Initializing SearchWindow");
		
		if(searchWindow != null) {
			searchWindow.dispose();
			searchWindow = null;
		}
		super(Main.getMain(), "Search", false);
		
		searchWindow = this;
		
		searchedItemsPanel = new SearchedItemsPanel();
		source = Main.getMain();
		
		setSize(source.getWidth() / 2, source.getHeight() / 2);
		setResizable(false);
		setUndecorated(true);
		setFocusable(true);
		
		JPanel searchPanel = new JPanel();
		getContentPane().add(searchPanel, BorderLayout.NORTH);
		
		searchTextField = new JTextField();
		searchTextField.setToolTipText("text");
		searchPanel.add(searchTextField);
		searchTextField.setColumns(10);
		
		add(emptyPanel, BorderLayout.CENTER);
		
		addSearchTextFieldEventListener();
		addFocusListener();
		
		int x = (int) getOwner().getLocationOnScreen().getX() + (getOwner().getWidth() / 2 + getWidth());
		int y = (int) getOwner().getLocationOnScreen().getY() + (getOwner().getHeight() / 2 + getHeight());
		setLocation(x, y);
		
		searchTextField.requestFocus();
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
	
	public void clear() {
//		refrehs the searched items panel
		searchedItemsPanel.listItems();
		remove(searchedItemsPanel);
		add(emptyPanel);
		refresh();
	}
	
	private void addFocusListener() {
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				return;
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				for(Window w : getOwnedWindows()) {
					w.dispose();
				}
			}
		});
	}
	
	public void destroyChildWindows() {
		requestFocus();
	}
	
}
