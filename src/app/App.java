package app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import app.excp.GlobalExceptionHandler;
import app.excp.DatabaseInitializationException;
import app.gui.Main;

import javax.swing.*;

public class App {
	private static final Logger logger = Logger.getLogger(App.class.getName());
    
    public static void main(String[] args) {
    	
    	// Setting the global exception handler for main app
    	Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        
        SwingUtilities.invokeLater(() -> {
			
			try {
				// Initializing the connection and statement
				Connection connection = DriverManager.getConnection("jdbc:h2:./src/data/dontforget", "sa", "");
				Statement stmt = connection.createStatement();

		        // Initialize the look and feel
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		        logger.info("Starting DontForget application...");

		        // Initialize database tables using crTables.sql
		        Path crTablesPath = Path.of("src/app/db/tables/crTables.sql");
//		        Path testRecords = Path.of("src/app/db/tables/crTables.sql");
		        if (Files.exists(crTablesPath)) {

		        	stmt.execute("RUNSCRIPT FROM 'src/app/db/tables/crTables.sql'");
//		        	stmt.execute("RUNSCRIPT FROM 'src/app/db/tables/testRecords.sql'");
		        	logger.info("Database tables initialized successfully using crTables.sql.");
		        	
		        } else {
		            logger.severe("crTables.sql file not found at " + crTablesPath.toAbsolutePath());
		        }
		        new Main(connection);
		        
		    }catch (SQLException e) {
				throw new DatabaseInitializationException();
			}
		    catch (Exception e){
		        e.printStackTrace();
		    }
			
		});
    }
}
