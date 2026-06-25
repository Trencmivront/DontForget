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
	private static ServerSocket serverSocket;
    
    public static void main(String[] args) {
    	// Try binding to the single-instance port immediately
        try {
            serverSocket = new ServerSocket(19999);
            logger.info("Successfully bound port 19999. Starting primary instance.");
        } catch (IOException e) {
            // Port already in use. Connect to the existing instance and ask it to show.
            logger.info("Another instance is running. Attempting to bring it to front...");
            try (Socket socket = new Socket("localhost", 19999);
                 OutputStream out = socket.getOutputStream()) {
                out.write("SHOW\n".getBytes());
                out.flush();
            } catch (IOException ioException) {
                logger.severe("Could not notify running instance: " + ioException.getMessage());
            }
            return; // Exit this secondary instance immediately
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
		        Path crTablesPath = Path.of("/usr/share/DontForget/db/crTables.sql");
		        if (Files.exists(crTablesPath)) {
//		        	stmt.execute("RUNSCRIPT FROM 'src/main/resources/db/tables/delTables.sql'");
		        	stmt.execute("RUNSCRIPT FROM '/usr/share/DontForget/db/crTables.sql'");
//		        	stmt.execute("RUNSCRIPT FROM '/usr/share/DontForget/db/testRecords.sql'");
		        	logger.info("Database tables initialized successfully using crTables.sql.");
		        	
//			        show window
			        new Main();
//			        Start background listener
					startSingleInstanceListener();
//			        initialize the notification manager
			        NotificationManager nm = new NotificationManager();
			        nm.initialize();
		        } else {
		            logger.severe("crTables.sql file not found at " + crTablesPath.toAbsolutePath());
		        }
		        
		    }catch (SQLException s) {
		    	s.printStackTrace();
				JOptionPane.showMessageDialog(new JDialog(), "ERROR: Couldn't initialize database.", "ok", JOptionPane.WARNING_MESSAGE);
			}
		    catch (Exception e){
				JOptionPane.showMessageDialog(new JDialog(), e.getMessage());
		        e.printStackTrace();
		    }
			
		});
        
//        Close the connection when app is terminated
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        	try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    logger.info("Database connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                    logger.info("Server socket connection closed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("Exiting DontForget app.");
        }));
    }
    
    private static void startSingleInstanceListener() {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    try (Socket clientSocket = serverSocket.accept();
                         BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                        String message = bf.readLine();
                        if ("SHOW".equals(message)) {
                            SwingUtilities.invokeLater(() -> {
                                if (Main.main != null) {
                                    Main.main.setVisible(true);
                                    Main.main.toFront();
                                    Main.main.requestFocus();
                                }
                            });
                        }
                    } catch (Exception e) {
                        if (!serverSocket.isClosed()) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                if (!serverSocket.isClosed()) {
                    logger.severe("Could not start single instance listener: " + e.getMessage());
                }
            }
        }).start();
    }
}
