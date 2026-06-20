package main.notify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WaylandNotification {

    public static void sendClickableNotification(String title, String body) {
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
                command.add("JavaApp");                         // app_name
                command.add("0");                               // replaces_id
                command.add("");                                // app_icon
                command.add(title);                             // summary
                command.add(body);                              // body
                command.add("['default', 'Open Application']"); // actions (default = clicking the body)
                command.add("{}");                              // hints
                command.add("0");                              // expire_timeout (default)
                Process process = new ProcessBuilder(command).start();

                // Listen to the command output
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // If the user clicks the notification body, 'default' is returned
                        if (line.contains("'default'")) {
                            break; 
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        sendClickableNotification("Click Me!", "Clicking this alert will open the main.");
    }
}
