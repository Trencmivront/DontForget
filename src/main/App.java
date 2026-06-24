package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
import main.notify.NotificationManager;

public class App {
	private static final Logger logger = Logger.getLogger(App.class.getName());
	public static Connection connection; 
    
    public static void main(String[] args) {
    	
    	// Try connecting to the running instance
        try (Socket socket = new Socket("localhost", 19999);
        		OutputStream out = socket.getOutputStream();) {
                out.write("SHOW".getBytes());
                out.flush();
            logger.info("Showing the app.");
            return; 
        } catch (IOException _) {
            logger.info("Starting app.");
        }
    	
    	System.setProperty("sun.java2d.uiScale", "2.0");
    	System.setProperty("awt.useSystemAAFontSettings", "on");
    	System.setProperty("swing.aatext", "true");
    	
    	if(Main.main != null) {
    		Main.main.setVisible(true);
    		return;
    	}

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
//		        initialize the notification manager
		        NotificationManager nm = new NotificationManager();
		        nm.initialize();
		        
		    }catch (SQLException s) {
		    	s.printStackTrace();
				JOptionPane.showMessageDialog(new JDialog(), "ERROR: App is running.", "ok", JOptionPane.WARNING_MESSAGE);
			}
		    catch (Exception e){
				JOptionPane.showMessageDialog(new JDialog(), e.getMessage());
		        e.printStackTrace();
		    }
			
//	        show window
	        new Main();
			startSingleInstanceListener();
		});
        
//        Close the connection when app is terminated
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (App.connection != null && !App.connection.isClosed()) {
                    App.connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
    }
    
    private static void startSingleInstanceListener() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(19999)) {
                while (true) {
                    try (Socket clientSocket = serverSocket.accept();
                    		BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                    	String message = bf.readLine();
//                    	If SHOW, then show?
                    	if("SHOW".equals(message)) {
                        SwingUtilities.invokeLater(() -> {
                            if (Main.main != null) {
                                Main.main.setVisible(true);
                                Main.main.toFront();
                                Main.main.requestFocus();
                            }
                        });
                    	}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                logger.severe("Could not start single instance listener: " + e.getMessage());
            }
        }).start();
    
    }
}
