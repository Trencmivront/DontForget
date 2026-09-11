package main.io.github.trencmivront.dontforget.enums;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/// Contains paths to icons located in resources folder.
public enum Icon {
	// priority icons
	RED_FALG(new ClassPathResource("icons/red_flag.png")),
	GREEN_FLAG(new ClassPathResource("icons/green_flag.png")),
	YELLOW_FLAG(new ClassPathResource("icons/yellow_flag.png")),
	NONE_FLAG(new ClassPathResource("icons/none_flag.png")),
	// icon for deleting tasks or projects buttons
	RED_TRASH_CAN(new ClassPathResource("icons/red_trash_can.png")),
	ORANGE_TRASH_CAN(new ClassPathResource("icons/orange_trash_can.png")),
	// dueDate icon
	CALENDAR(new ClassPathResource("icons/calendar.png")),
	
	PROHIBITION(new ClassPathResource("icons/prohibition.png")),
	// TaskWindow reminderBtn icons
	ADD_REMINDER(new ClassPathResource("icons/add_reminder.png")),
	RINGING(new ClassPathResource("icons/ringing.png")),
	
	// Main window navigation buttons icons
	REMINDERS(new ClassPathResource("icons/reminders.png")),
	INBOX(new ClassPathResource("icons/inbox.png")),
	TAGS(new ClassPathResource("icons/tags.png")),
	TODAY(new ClassPathResource("icons/today.png")),
	PLUS(new ClassPathResource("icons/plus.png")),
	
	// Script icons
	ADD_FILE(new ClassPathResource("icons/add_file.png")),
	FILE(new ClassPathResource("icons/file.png")),
	
	// TaskWindow tagBtn icon
	TAG(new ClassPathResource("icons/tag.png"));
	
	private ImageIcon icon;
	private final Logger logger = LoggerFactory.getLogger(Icon.class.getName());
	
	private Icon(ClassPathResource resourceUrl) {
		 try {
			this.icon = new ImageIcon(resourceUrl.getURL());
		 } catch (IOException _) {
			 logger.warn("{}: Image not found in the path.", this);
		 }
	}
	
	public ImageIcon getSmallIcon() {
		Image scaled = icon.getImage().
				getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}
	
	public ImageIcon getMediumIcon() {
		Image scaled = icon.getImage().
				getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}
}
