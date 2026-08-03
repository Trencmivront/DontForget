package main.java.notify;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.controllers.InboxController;
import main.java.custom.SpringContext;
import main.java.dto.InboxDTO;
import main.java.dto.ReminderDTO;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class NotificationWorker implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(NotificationWorker.class.getName());
    private final InboxController inboxController = SpringContext.getBean(InboxController.class);
//	id of the task to open
	private Long id;
//	title of the reminder
	private String title;
//	message of the reminder
	private String message;
	private ReminderDTO reminderDTO;
		
	public NotificationWorker(ReminderDTO reminderDTO, String title, String message) {
		super();
		this.reminderDTO = reminderDTO;
		this.id = reminderDTO.getTaskId();
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
		AudioInputStream audioInputStream = null;
		try {
			File audioFile = new File("src/main/resources/sounds/dry-pop-up.wav");
			if (audioFile.exists()) {
				logger.info("Loading notification sound: {}", audioFile.getAbsolutePath());
				audioInputStream = AudioSystem.getAudioInputStream(audioFile);
				final Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				logger.info("Playing notification sound...");
				clip.start();
				
				NotificationFactory.getNotificationService().sendNotification(id, title, message);
//				reschedule it if it is recurring
				NotificationManager.getInstance().scheduleReminder(reminderDTO);
				
                logger.info("Saving notification message to inbox DB: {}", message);
                inboxController.createMessage((new InboxDTO(message)));
				
				Thread thread = new Thread(() -> {
//					want to send notification while song is playing
					long playDurationMs = clip.getMicrosecondLength() / 1000;
					logger.info("Sleeping {} ms for audio playback.", playDurationMs);
					try {
						Thread.sleep(playDurationMs);
					} catch (InterruptedException _) {
						clip.close();
						logger.warn("Sound play thread is interrupted.");
					}
				});

				thread.start();
				
			} else {
				logger.warn("Notification sound file not found at {}. Sending notification without sound.", audioFile.getAbsolutePath());
				NotificationFactory.getNotificationService().sendNotification(id, title, message);
			}
			
		} catch (Exception e) {
			logger.error("Exception occurred in NotificationWorker : {}, {}", e.getClass(), e.getMessage());
		} finally {
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
