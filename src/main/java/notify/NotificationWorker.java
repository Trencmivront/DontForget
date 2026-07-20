package main.java.notify;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class NotificationWorker implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(NotificationWorker.class.getName());
//	id of the task to open
	private Long id;
//	title of the reminder
	private String title;
//	message of the reminder
	private String message;
	
	public NotificationWorker(Long id, String title, String message) {
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
		logger.info("Executing {}", this.getClass());
		Clip clip = null;
		AudioInputStream audioInputStream = null;
		try {
			File audioFile = new File("src/main/resources/sounds/dry-pop-up.wav");
			if (audioFile.exists()) {
				logger.info("Loading notification sound: {}", audioFile.getAbsolutePath());
				audioInputStream = AudioSystem.getAudioInputStream(audioFile);
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				logger.info("Playing notification sound...");
				clip.start();
				
				WaylandNotification.sendNotification(id, title, message);
				
				long playDurationMs = clip.getMicrosecondLength() / 1000;
				logger.info("Sleeping {} ms for audio playback.", playDurationMs);
				Thread.sleep(playDurationMs);
			} else {
				logger.warn("Notification sound file not found at {}. Sending notification without sound.", audioFile.getAbsolutePath());
				WaylandNotification.sendNotification(id, title, message);
			}
		} catch (Exception e) {
			logger.error("Exception occurred in NotificationWorker run: {}", e.getMessage());
			e.printStackTrace();
		} finally {
			if (clip != null) {
				try {
					clip.close();
				} catch (Exception e) {
					logger.warn("Error closing audio clip: {}", e.getMessage());
				}
			}
			if (audioInputStream != null) {
				try {
					audioInputStream.close();
				} catch (Exception e) {
					logger.warn("Error closing audio input stream: {}", e.getMessage());
				}
			}
		}
	}
}
