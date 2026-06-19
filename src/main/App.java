package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.swing.*;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import main.gui.Main;

public class App {
	private static final Logger logger = Logger.getLogger(App.class.getName());
	public static Connection connection; 
    
    public static void main(String[] args) {
    	System.setProperty("sun.java2d.uiScale", "2.0");
    	System.setProperty("awt.useSystemAAFontSettings", "on");
    	System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() ->{
        	
			try {
				// Initializing the connection and statement
				connection = DriverManager.getConnection("jdbc:h2:./src/data/dontforget", "sa", "");
				Statement stmt = connection.createStatement();

		        // Initialize the look and feel
		        UIManager.setLookAndFeel(new FlatMacDarkLaf());

		        logger.info("Starting DontForget application...");

		        // Initialize database tables using crTables.sql
		        Path crTablesPath = Path.of("src/main/db/tables/crTables.sql");
		        if (Files.exists(crTablesPath)) {
//		        	stmt.execute("RUNSCRIPT FROM 'src/main/db/tables/delTables.sql'");
		        	stmt.execute("RUNSCRIPT FROM 'src/main/db/tables/crTables.sql'");
//		        	stmt.execute("RUNSCRIPT FROM 'src/main/db/tables/testRecords.sql'");
		        	logger.info("Database tables initialized successfully using crTables.sql.");
		        	
		        } else {
		            logger.severe("crTables.sql file not found at " + crTablesPath.toAbsolutePath());
		        }
		        Main main = new Main();
		        
		        // to open and close connection when needed
		        main.addWindowListener(new WindowAdapter() {
		        	@Override
					public void windowClosing(WindowEvent e) {
						try {		
							App.connection.close();
						}catch (SQLException s) {
							s.printStackTrace();
						}
					}
					@Override
					public void windowOpened(WindowEvent e) {
						try {
							App.connection = DriverManager.getConnection("jdbc:h2:./src/data/dontforget", "sa", "");
						}catch (SQLException s) {
							s.printStackTrace();
						}
					}
				});
		        
		    }catch (SQLException s) {
		    	s.printStackTrace();
				JOptionPane.showMessageDialog(new JDialog(), "Error while initializing database.");
			}
		    catch (Exception e){
				JOptionPane.showMessageDialog(new JDialog(), e.getMessage());
		        e.printStackTrace();
		    }finally {
		    	try {
					App.connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
			
		});
    }
}
