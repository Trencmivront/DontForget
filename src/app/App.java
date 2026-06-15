package app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import app.gui.Main;

import javax.swing.*;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class App {
	private static final Logger logger = Logger.getLogger(App.class.getName());
	public static Connection connection; 
    
    public static void main(String[] args) {
    	System.setProperty("sun.java2d.uiScale", "2.0");
    	System.setProperty("awt.useSystemAAFontSettings", "on");
    	System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
			
			try {
				// Initializing the connection and statement
				connection = DriverManager.getConnection("jdbc:h2:./src/data/dontforget", "sa", "");
				Statement stmt = connection.createStatement();

		        // Initialize the look and feel
		        UIManager.setLookAndFeel(new FlatMacDarkLaf());

		        logger.info("Starting DontForget application...");

		        // Initialize database tables using crTables.sql
		        Path crTablesPath = Path.of("src/app/db/tables/crTables.sql");
		        if (Files.exists(crTablesPath)) {
		        	stmt.execute("RUNSCRIPT FROM 'src/app/db/tables/delTables.sql'");
		        	stmt.execute("RUNSCRIPT FROM 'src/app/db/tables/crTables.sql'");
		        	stmt.execute("RUNSCRIPT FROM 'src/app/db/tables/testRecords.sql'");
		        	logger.info("Database tables initialized successfully using crTables.sql.");
		        	
		        } else {
		            logger.severe("crTables.sql file not found at " + crTablesPath.toAbsolutePath());
		        }
		        new Main();
		        
		    }catch (SQLException s) {
		    	s.printStackTrace();
				JOptionPane.showMessageDialog(new JDialog(), "Error while initializing database.");
			}
		    catch (Exception e){
				JOptionPane.showMessageDialog(new JDialog(), e.getMessage());
		        e.printStackTrace();
		    }
			
		});
    }
}
