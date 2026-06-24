package main.notify;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
//		Playing notification sound and send notification
		try{
			File audioFile = new File("src/resources/sound/dry-pop-up.wav");
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
			
			Clip clip = AudioSystem.getClip();
			
			clip.open(audioInputStream);
			
			clip.start();
			WaylandNotification.sendNotification(title, message);
			Thread.sleep(clip.getMicrosecondLength() / 1000);
			
			clip.close();
			audioInputStream.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
