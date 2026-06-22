package main.notify;

public class NotificationWorker implements Runnable{
//	id of the task to open
	private int id;
//	title of the reminder
	private String title;
//	message of the reminder
	private String message;
	
	public NotificationWorker(int id, String title, String message) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
	}

	public NotificationWorker(String title, String message) {
		super();
		this.title = title;
		this.message = message;
	}

	@Override
	public void run() {
		WaylandNotification.sendNotification(title, message);
	}
}
