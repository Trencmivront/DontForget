package main.notify;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import main.gui.Main;
import main.services.inbox.CreateMessageService;

public class WaylandNotification {

    public static void sendNotification(String title, String body) {
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
                Process process = new ProcessBuilder(command).start();

                // Listen to the command output
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
//                  Send message to inbox
                	CreateMessageService.execute(body);
                    while ((line = reader.readLine()) != null) {
                        // If the user clicks the notification body, 'default' is returned
                        if (line.contains("'default'")) {
                            SwingUtilities.invokeLater(() -> {
                                if (Main.main != null) {
                                    if (Main.main.getState() == Frame.ICONIFIED) {
                                        Main.main.setState(Frame.NORMAL);
                                    }
                                    Main.main.setVisible(true);
                                    Main.main.toFront();
                                    Main.main.requestFocus();
                                }
                            });
                            break; 
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
