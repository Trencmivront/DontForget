package main.java.notify;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;

import main.java.custom.SpringContext;
import main.java.controllers.InboxController;
import main.java.gui.Main;

public class WaylandNotification {
    private final InboxController inboxController = SpringContext.getBean(InboxController.class);

    public WaylandNotification() {
    }

    private static final Logger logger = LoggerFactory.getLogger(WaylandNotification.class.getName());

    public void sendNotification(Long taskId, String title, String body) {
        logger.info("Preparing to send notification. Task ID: {}, Title: {}", taskId, title);
        // Run in a new thread so it doesn't block your main application
        new Thread(() -> {
            try {
                // Construct the gdbus command to call the Notification server
                List<String> command = new ArrayList<>();
                command.add("gdbus");
                command.add("call");
                command.add("--session");
                command.add("--dest=org.freedesktop.Notifications");
                command.add("--object-path=/org/freedesktop/Notifications");
                command.add("--method=org.freedesktop.Notifications.Notify");
                
                // Arguments: app_name, replaces_id, app_icon, summary, body, actions, hints, expire_timeout
                command.add("DontForget");                         // app_name
                command.add("0");                               // replaces_id
                command.add("");                                // app_icon
                command.add(title);                             // summary
                command.add(body);                              // body
                command.add("['default', 'Open Application']"); // actions (default = clicking the body)
                command.add("{}");                              // hints
                command.add("0");                              // expire_timeout (default)
                
                logger.info("Executing notification process command: {}", String.join(" ", command));
                Process process = new ProcessBuilder(command).start();

                // Listen to the command output
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    logger.info("Saving notification message to inbox DB: {}", body);
                    inboxController.createMessage(body);

                    while ((line = reader.readLine()) != null) {
                        logger.info("gdbus response: {}", line);
                        // If the user clicks the notification body, 'default' is returned
                        if (line.contains("'default'")) {
                            logger.info("User clicked the default action of notification.");
                            SwingUtilities.invokeLater(() -> {
                                if (Main.getMain() != null) {
                                    Main.getMain().setState(Frame.NORMAL); // De-iconify if minimized
                                    Main.getMain().setVisible(true);
                                    Main.getMain().toFront();
                                    Main.getMain().requestFocus();
                                }
                            });
                            break; 
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error sending Wayland notification: {}", e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
