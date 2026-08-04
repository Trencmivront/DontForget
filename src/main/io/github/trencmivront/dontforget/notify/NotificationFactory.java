package main.io.github.trencmivront.dontforget.notify;

import main.io.github.trencmivront.dontforget.inter.NotificationService;
import main.io.github.trencmivront.dontforget.notify.services.DBusNotificationService;
import main.io.github.trencmivront.dontforget.notify.services.MacosNotificationService;
import main.io.github.trencmivront.dontforget.notify.services.WindowsNotificationService;

public class NotificationFactory {
//	Get the required notification by looking OS value
	public static NotificationService getNotificationService() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))  return new WindowsNotificationService();
        if (os.contains("mac"))  return new MacosNotificationService();
        return new DBusNotificationService();
    }
}
	
