package main.java;

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
    
    private static void startSingleInstanceListener() {
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
    	
    	try {
    		JsonNode node = mapper.readTree(settingsFile);
    		checkIconSet(node);
    		System.setProperty("sun.java2d.uiScale", checkUiScaleSet(node));
    		System.setProperty("awt.useSystemAAFontSettings", checkSystemAAFontSet(node));
    		System.setProperty("swing.aatext", checkSwingAATextSet(node));
    	}catch (IOException e) {
			e.printStackTrace();
			System.setProperty("sun.java2d.uiScale", "2.0");
			System.setProperty("awt.useSystemAAFontSettings", "on");
			System.setProperty("swing.aatext", "true");
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

    private static String checkUiScaleSet(JsonNode node) {
    	final String defaultValue = "2.0";
    	JsonNode scaleNode = node.get("uiScale");
    	if (scaleNode != null && !scaleNode.isNull()) {
    		try {
    			double scale = Double.parseDouble(scaleNode.asText());
    			if (scale > 0) {
    				return scaleNode.asText();
    			}
    			logger.warn("uiScale must be above 0, falling back to default {}.", defaultValue);
    		} catch (NumberFormatException _) {
    			logger.warn("Invalid uiScale value '{}', falling back to default {}.", scaleNode.asText(), defaultValue);
    		}
    	} else {
    		setKeyValue("uiScale", defaultValue);
    	}
    	return defaultValue;
    }

    private static String checkSystemAAFontSet(JsonNode node) {
    	final String defaultValue = "on";
    	JsonNode aaNode = node.get("awtUseSystemAAFontSettings");
    	if (aaNode != null && !aaNode.isNull()) {
    		return aaNode.asText();
    	}
    	setKeyValue("awtUseSystemAAFontSettings", defaultValue);
    	return defaultValue;
    }

    private static String checkSwingAATextSet(JsonNode node) {
    	final String defaultValue = "true";
    	JsonNode aaNode = node.get("swingAAText");
    	if (aaNode != null && !aaNode.isNull()) {
    		return aaNode.asText();
    	}
    	setKeyValue("swingAAText", defaultValue);
    	return defaultValue;
    }
    
    private static void setKeyValue(String key, Object value) {
        try {
            ObjectNode rootNode = mapper.createObjectNode();
            // put object and let json cast it
            rootNode.putPOJO(key, value);
            mapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, rootNode);
            logger.info("Saved settings.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
