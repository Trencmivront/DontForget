package main.java.notify.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.inter.NotificationService;

public class WindowsNotificationService implements NotificationService{

    private static final Logger logger = LoggerFactory.getLogger(WindowsNotificationService.class.getName());

    public void sendNotification(Long taskId, String title, String body) {
        logger.info("Preparing to send notification. Task ID: {}, Title: {}", taskId, title);
        // Run in a new thread so it doesn't block the main application
        new Thread(() -> {
            try {
                // Use PowerShell with the Windows Runtime (WinRT) toast notification API.
                // This works on Windows 10/11 without any third-party dependencies.
                String escapedTitle = title.replace("'", "''");
                String escapedBody  = body.replace("'", "''");

                String psScript = String.format(
                    "[Windows.UI.Notifications.ToastNotificationManager, Windows.UI.Notifications, ContentType=WindowsRuntime] | Out-Null;" +
                    "[Windows.Data.Xml.Dom.XmlDocument, Windows.Data.Xml.Dom.XmlDocument, ContentType=WindowsRuntime] | Out-Null;" +
                    "$template = [Windows.UI.Notifications.ToastTemplateType]::ToastText02;" +
                    "$xml = [Windows.UI.Notifications.ToastNotificationManager]::GetTemplateContent($template);" +
                    "$nodes = $xml.GetElementsByTagName('text');" +
                    "$nodes.Item(0).AppendChild($xml.CreateTextNode('%s')) | Out-Null;" +
                    "$nodes.Item(1).AppendChild($xml.CreateTextNode('%s')) | Out-Null;" +
                    "$toast = [Windows.UI.Notifications.ToastNotification]::new($xml);" +
                    "[Windows.UI.Notifications.ToastNotificationManager]::CreateToastNotifier('DontForget').Show($toast);",
                    escapedTitle, escapedBody
                );

                List<String> command = new ArrayList<>();
                command.add("powershell.exe");
                command.add("-NoProfile");
                command.add("-NonInteractive");
                command.add("-Command");
                command.add(psScript);

                logger.info("Executing notification process command via PowerShell");
                Process process = new ProcessBuilder(command).start();

                // Read stdout (PowerShell may emit output on success/failure)
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info("PowerShell response: {}", line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    logger.warn("PowerShell notification process exited with code: {}", exitCode);
                } else {
                    logger.info("Windows toast notification sent successfully for task ID: {}", taskId);
                }
//              TODO: Add action on click over notification for Windows

            } catch (Exception e) {
                logger.error("Error sending Windows notification: {}", e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
