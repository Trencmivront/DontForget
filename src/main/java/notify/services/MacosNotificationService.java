package main.java.notify.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.controllers.InboxController;
import main.java.custom.SpringContext;
import main.java.dto.InboxDTO;
import main.java.inter.NotificationService;

public class MacosNotificationService implements NotificationService{
    private final InboxController inboxController = SpringContext.getBean(InboxController.class);

    private static final Logger logger = LoggerFactory.getLogger(MacosNotificationService.class.getName());

    public void sendNotification(Long taskId, String title, String body) {
        logger.info("Preparing to send notification. Task ID: {}, Title: {}", taskId, title);
        // Run in a new thread so it doesn't block the main application
        new Thread(() -> {
            try {
                // Use osascript (AppleScript) to deliver a native macOS notification.
                // The 'display notification' command works on macOS 10.9 (Mavericks) and later.
                String escapedTitle = title.replace("\\", "\\\\").replace("\"", "\\\"");
                String escapedBody  = body.replace("\\", "\\\\").replace("\"", "\\\"");

                String appleScript = String.format(
                    "display notification \"%s\" with title \"%s\" subtitle \"DontForget\"",
                    escapedBody, escapedTitle
                );

                List<String> command = new ArrayList<>();
                command.add("osascript");
                command.add("-e");
                command.add(appleScript);

                logger.info("Executing notification process command via osascript");
                Process process = new ProcessBuilder(command).start();

                // Listen to the command output
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    logger.info("Saving notification message to inbox DB: {}", body);
                    inboxController.createMessage(new InboxDTO(body));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info("osascript response: {}", line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    logger.warn("osascript notification process exited with code: {}", exitCode);
                } else {
                    logger.info("macOS notification sent successfully for task ID: {}", taskId);
                }
//              TODO: Find a way to display app

            } catch (Exception e) {
                logger.error("Error sending macOS notification: {}", e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
