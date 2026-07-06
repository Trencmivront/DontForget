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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    	
    	if(Main.main != null) {
    		Main.main.setVisible(true);
    		return;
    	}

    	applySettings();
    	
        SwingUtilities.invokeLater(() ->{
//			Displaying app
			try {
		        logger.info("Starting DontForget application...");
		        // Initialize the look and feel
		        UIManager.setLookAndFeel(new FlatMacDarkLaf());
		        
//		        show window
		        new Main();
//		        Start background listener
				startSingleInstanceListener();
		        
//		        initialize the notification manager
		        NotificationManager nm = new NotificationManager();
		        nm.initialize();
			} catch (Exception e) {
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
        logger.info("Background listener started.");
    }
    
    private static void applySettings() {
        boolean initialized = false;
        Path settingsPath = Path.of("src/data/settings/settings.json");
        ObjectMapper mapper = new ObjectMapper();
        
        if (!Files.exists(settingsPath)) {
            try {
                Files.createDirectories(settingsPath.getParent());
                ObjectNode rootNode = mapper.createObjectNode();
                rootNode.put("isDatabaseInitialized", false);
                mapper.writerWithDefaultPrettyPrinter().writeValue(settingsPath.toFile(), rootNode);
                logger.info("settings.json did not exist. Created and initialized with isDatabaseInitialized=false.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JsonNode rootNode = mapper.readTree(settingsPath.toFile());
                JsonNode initNode = rootNode.get("isDatabaseInitialized");
                if (initNode != null && initNode.asBoolean()) {
                    initialized = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (initialized) {
            try {
                connection = DriverManager.getConnection("jdbc:h2:./src/data/db/dontforget", "sa", "");
                logger.info("Database connection established. Skipped database initialization scripts.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new JDialog(), "ERROR: Couldn't connect to database.", "ok", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            initializeDatabase();
            try {
                ObjectNode rootNode = mapper.createObjectNode();
                rootNode.put("isDatabaseInitialized", true);
                mapper.writerWithDefaultPrettyPrinter().writeValue(settingsPath.toFile(), rootNode);
                logger.info("Saved isDatabaseInitialized=true to settings.json using Jackson");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        System.setProperty("sun.java2d.uiScale", "2.0");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
    
    private static void initializeDatabase() {
    	
//    	initializing the database
		try {
			Statement stmt = connection.createStatement();

	        // Initialize database tables using crTables.sql
	        Path crTablesPath = Path.of("/usr/share/DontForget/db/crTables.sql");
	        if (Files.exists(crTablesPath)) {
	        	stmt.execute("RUNSCRIPT FROM '/usr/share/DontForget/db/delTables.sql'");
	        	stmt.execute("RUNSCRIPT FROM '/usr/share/DontForget/db/crTables.sql'");
	        	stmt.execute("RUNSCRIPT FROM '/usr/share/DontForget/db/testRecords.sql'");
	        	logger.info("Database tables initialized successfully.");
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
		
    }
}
