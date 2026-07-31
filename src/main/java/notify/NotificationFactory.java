package main.java.notify;

import main.java.inter.NotificationService;
import main.java.notify.services.DBusNotificationService;
import main.java.notify.services.MacosNotificationService;
import main.java.notify.services.WindowsNotificationService;

public class NotificationFactory {
//	Get the required notification by looking OS value
	public static NotificationService getNotificationService() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))  return new WindowsNotificationService();
        if (os.contains("mac"))  return new MacosNotificationService();
        return new DBusNotificationService();
    }
}
	
