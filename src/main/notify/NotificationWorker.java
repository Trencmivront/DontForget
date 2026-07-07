package main.notify;

import java.io.File;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class NotificationWorker implements Runnable{
	private static final Logger logger = Logger.getLogger(NotificationWorker.class.getName());
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
		logger.info("Starting NotificationWorker run for task ID: " + id + ", Title: " + title);
		Clip clip = null;
		AudioInputStream audioInputStream = null;
		try {
			File audioFile = new File("src/main/resources/sounds/dry-pop-up.wav");
			if (audioFile.exists()) {
				logger.info("Loading notification sound: " + audioFile.getAbsolutePath());
				audioInputStream = AudioSystem.getAudioInputStream(audioFile);
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				logger.info("Playing notification sound...");
				clip.start();
				
				WaylandNotification.sendNotification(id, title, message);
				
				long playDurationMs = clip.getMicrosecondLength() / 1000;
				logger.info("Sleeping " + playDurationMs + " ms for audio playback.");
				Thread.sleep(playDurationMs);
			} else {
				logger.warning("Notification sound file not found at " + audioFile.getAbsolutePath() + ". Sending notification without sound.");
				WaylandNotification.sendNotification(id, title, message);
			}
		} catch (Exception e) {
			logger.severe("Exception occurred in NotificationWorker run: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (clip != null) {
				try {
					clip.close();
				} catch (Exception e) {
					logger.warning("Error closing audio clip: " + e.getMessage());
				}
			}
			if (audioInputStream != null) {
				try {
					audioInputStream.close();
				} catch (Exception e) {
					logger.warning("Error closing audio input stream: " + e.getMessage());
				}
			}
		}
	}
}
