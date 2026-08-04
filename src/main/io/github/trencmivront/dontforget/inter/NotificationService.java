package main.io.github.trencmivront.dontforget.inter;

public interface NotificationService {
	public void sendNotification(Long taskId, String title, String body);
}
