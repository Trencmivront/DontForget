package main.java;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import main.java.gui.Main;
import main.java.notify.NotificationManager;

@SpringBootApplication
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class.getName());
	private static ServerSocket serverSocket;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final File settingsFile = Path.of("src/data/settings/settings.json").toFile();
    
	public static void main(String[] args) {
	    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
	        logger.error("Uncaught exception in thread {}: {}", thread.getName(), throwable.getMessage(), throwable);
	    });
		startApp(args);
	}

	private static void startApp(String[] args) {
		// Try binding to the single-instance port
		showExistingWindow();

		applySettings();
		
		// Start Spring Boot
		new SpringApplicationBuilder(App.class).
		headless(false).
		run(args);
		
		SwingUtilities.invokeLater(() ->{
//			Displaying app
			try {
				logger.info("Starting DontForget application...");
				// Initialize the look and feel
				
//				show window
				new Main();
//				Start background listener
				startSingleInstanceListener();
				
//				initialize the notification manager
				NotificationManager nm = new NotificationManager();
				nm.initialize();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(new JDialog(), e.getMessage(), "ok", JOptionPane.WARNING_MESSAGE);
				
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
    
    public static void startSingleInstanceListener() {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    readPortMessage();
                }
            } catch (Exception e) {
                if (!serverSocket.isClosed()) {
                    logger.error("Could not start single instance listener: {}", e.getMessage());
                }
            }
        }).start();
        logger.info("Background listener started.");
    }
    
    private static void showExistingWindow() {
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
				logger.error("Could not notify running instance: {}", ioException.getMessage());
			}
			return;
		}
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
//    	get the JsonNode from file
    	try {
    		if(!settingsFile.exists()) {
    			settingsFile.getParentFile().mkdirs();
				mapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, mapper.createObjectNode());
    		}
    		final JsonNode node = mapper.readTree(settingsFile);
    		
//        	read and insert values
        	checkIconSet(node);
        	checkSystemAAFontSet(node);
        	checkSwingAATextSet(node);
//        	apply saved scale BEFORE AWT initializes
        	if (!applyScaleFromSettings(node)) {
//        		no saved scale — detect after AWT starts and save for next launch
        		SwingUtilities.invokeLater(App::detectAndSaveScale);
        	}
    	}catch (IOException e) {
    		logger.warn("applySettings: {}", e.getMessage());
		}
    	
    	FlatMacDarkLaf.setup();
    }
    
    private static void checkIconSet(JsonNode node) {
    	JsonNode iconNode = node.get("isIconSet");
    	boolean isIconSet = iconNode != null && iconNode.asBoolean();
    	
    	if (!isIconSet) {
    		try {
    			Path src = Path.of("src/main/resources/dontforget.png");
    			Path dest = Paths.get(System.getProperty("user.home"), ".local/share/icons/hicolor/32x32/apps/dontforget.png");
    			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
    			logger.info("Icon copied to system icons directory.");
        		setKeyValue("isIconSet", true);
    		} catch (Exception e) {
    			logger.warn("Could not copy icon to system icons directory, skipping: {}", e.getMessage());
    		}
    	}
    }

//  Returns true if a valid scale was found in settings and applied
    private static boolean applyScaleFromSettings(JsonNode node) {
    	JsonNode scaleNode = node.get("uiScale");
    	if (scaleNode != null && !scaleNode.isNull()) {
    		try {
    			double scale = Double.parseDouble(scaleNode.asText());
    			if (scale > 0) {
    				long rounded = Math.round(scale);
    				System.setProperty("sun.java2d.uiScale", String.valueOf(rounded));
    				logger.info("Applied uiScale: {}", rounded);
    				return true;
    			}
    			logger.warn("uiScale must be above 0, detecting from screen.");
    		} catch (NumberFormatException _) {
    			logger.warn("Invalid uiScale value '{}', detecting from screen.", scaleNode.asText());
    		}
    	}
    	return false;
    }

//  Runs in invokeLater — safe to use Toolkit here
    private static void detectAndSaveScale() {
    	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    	final long scale = Math.round(screen.getWidth() / screen.getHeight());
    	setKeyValue("uiScale", scale);
    	logger.info("Detected screen {}x{}, saved uiScale={} for next launch.", screen.width, screen.height, scale);
    }


    private static void checkSystemAAFontSet(JsonNode node) {
    	final String defaultValue = "on";
    	JsonNode aaNode = node.get("awtUseSystemAAFontSettings");
    	if (aaNode != null && !aaNode.isNull()) {
    		System.setProperty("awt.useSystemAAFontSettings", aaNode.asText());
    		return;
    	}
    	System.setProperty("awt.useSystemAAFontSettings", defaultValue);
    	setKeyValue("awtUseSystemAAFontSettings", defaultValue);
    }

    private static void checkSwingAATextSet(JsonNode node) {
    	final String defaultValue = "true";
    	JsonNode aaNode = node.get("swingAAText");
    	if (aaNode != null && !aaNode.isNull()) {
    		System.setProperty("swing.aatext", aaNode.asText());
    		return;
    	}
    	System.setProperty("swing.aatext", defaultValue);
    	setKeyValue("swingAAText", defaultValue);
    }
    
    private static void setKeyValue(String key, Object value) {
        try {
            ObjectNode rootNode = settingsFile.exists() ?
            		(ObjectNode) mapper.readTree(settingsFile) : 
            			mapper.createObjectNode();
            // put object and let json cast it
            rootNode.putPOJO(key, value);
            mapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, rootNode);
            logger.info("Saved {} => {}", key, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
