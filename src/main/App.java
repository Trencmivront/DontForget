package main;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import main.io.github.trencmivront.dontforget.custom.SettingsManager;
import main.io.github.trencmivront.dontforget.gui.Main;
import main.io.github.trencmivront.dontforget.notify.NotificationManager;

@SpringBootApplication
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class.getName());
	private static ServerSocket serverSocket;
	
	private static final SettingsManager settingsManager = new SettingsManager();

	public static void main(String[] args) {
	    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
	        logger.error("Uncaught exception in thread {}: {}", thread.getName(), throwable.getMessage(), throwable);
	    });
	    
	    try {
	        // Initialize your app normally
	    	startApp(args);
	    } catch (Throwable e) {
	        showCompatibilityAlert(e.getMessage() + 
	        		(e instanceof ClassNotFoundException ? " This version of Java is incompatible. The app is built in Java 25, try that instead.":""));
	    }
	}
	
	private static void showCompatibilityAlert(String message) {
	    Frame frame = new Frame();
	    frame.setVisible(false);
	    Dialog dialog = new Dialog(frame, "Error", false);
	    Label label = new Label(message);
	    label.setSize(350, 150);
	    dialog.add(label);
	    dialog.setSize(400, 200);
	    dialog.setLocationRelativeTo(null);
//	    The dialog doesn't close when I press "x". So I added this listener for it.
	    dialog.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            dialog.dispose();
	            frame.dispose();
	        }
	    });
	    
	    dialog.setVisible(true);
	}

	private static void startApp(String[] args) {
		// Try binding to the single-instance port
		if(showExistingWindow()) {
//			if it connects, prevent further execution
			return;
		}
		
		// Start Spring Boot
		new SpringApplicationBuilder(App.class).
		headless(false).
		run(args);
		
		// Initialize settings
		applySettings();
				
		SwingUtilities.invokeLater(() ->{
//			Displaying app
			try {
				logger.info("Starting DontForget application...");
				
//				show window
				Main mainWindow = new Main();
//				Apply close behaviour based on setting
				if (settingsManager.isRunOnBackground()) {
//					Keep the JVM alive when the window is closed; user can re-open via tray/single-instance
					mainWindow.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
					logger.info("runOnBackground=true: window will hide on close.");
				} else {
					mainWindow.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
					logger.info("runOnBackground=false: app will exit on close.");
				}
//				Start background listener
				startSingleInstanceListener();
				
//				initialize the notification manager
				NotificationManager.getInstance().initialize();
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
    
    private static boolean showExistingWindow() {
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
			return true;
		}
		return false;
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
    	settingsManager.validateAndSet();
    }

}
