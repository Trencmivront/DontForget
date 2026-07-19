package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import main.gui.Main;
import main.notify.NotificationManager;

@SpringBootApplication
public class App {
	private static final Logger logger = Logger.getLogger(App.class.getName());
	private static ServerSocket serverSocket;
    
	public static void main(String[] args) {
		startApp(args);
	}

	public static void main() {
		startApp(new String[0]);
	}

	private static void startApp(String[] args) {
		// Try binding to the single-instance port immediately
		try {
			serverSocket = new ServerSocket(19999);
			logger.info("Successfully bound port 19999. Starting primary instance.");
		} catch (IOException _) {
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

		applySettings();

		// Start Spring Boot Context
		SpringApplication.run(App.class, args);
		
		SwingUtilities.invokeLater(() ->{
//			Displaying app
			try {
				logger.info("Starting DontForget application...");
				// Initialize the look and feel
				UIManager.setLookAndFeel(new FlatMacDarkLaf());
				
//				show window
				new Main();
//				Start background listener
				startSingleInstanceListener();
				
//				initialize the notification manager
				NotificationManager nm = new NotificationManager();
				nm.initialize();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JDialog(), e.getMessage(), "ok", JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}
			
		});
		
//        Close the connection when app is terminated
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("Database connection closed.");
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
                    readPortMessage();
                }
            } catch (Exception e) {
                if (!serverSocket.isClosed()) {
                    logger.severe("Could not start single instance listener: " + e.getMessage());
                }
            }
        }).start();
        logger.info("Background listener started.");
    }
    
    private static void readPortMessage() {
    	try (Socket clientSocket = serverSocket.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
               String message = bf.readLine();
               if ("SHOW".equals(message)) {
                   SwingUtilities.invokeLater(() -> {
                       if (Main.getMain() != null) {
                           Main.getMain().setVisible(true);
                           Main.getMain().toFront();
                           Main.getMain().requestFocus();
                       }
                   });
               }
           } catch (Exception e) {
               if (!serverSocket.isClosed()) {
                   e.printStackTrace();
               }
           }
    }
    
    private static void applySettings() {
    	
    	Path settingsPath = Path.of("src/data/settings/settings.json");
    	ObjectMapper mapper = new ObjectMapper();
    	
    	checkDatabaseInitialized(settingsPath, mapper);
        
        System.setProperty("sun.java2d.uiScale", "2.0");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
    
    private static void checkDatabaseInitialized(Path settingsPath, ObjectMapper mapper) {
    	
    	boolean value = false;
        String key = "isDatabaseInitialized";
        
        if (!Files.exists(settingsPath)) {
            try {
                Files.createDirectories(settingsPath.getParent());
                setKeyValue(key, false, mapper, settingsPath);
                logger.info("settings.json did not exist. Created and initialized.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
//            	Get the value of the key from this file,
//            	take it as boolean, and deault value is "false".
                value = mapper.
                		readTree(settingsPath.toFile()).
                		path(key).
                		asBoolean(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (value) {
            logger.info("Database connection established.");
        }
        logger.info("Database connection established. Initializing database...");
        setKeyValue(key, true, mapper, settingsPath);
    }
    
    private static void setKeyValue(String key, Object value, ObjectMapper mapper, Path settingsPath) {
        try {
            ObjectNode rootNode = mapper.createObjectNode();
            // put object and let json cast it
            rootNode.putPOJO(key, value);
            mapper.writerWithDefaultPrettyPrinter().writeValue(settingsPath.toFile(), rootNode);
            logger.info("Saved settings.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
