package main.services.reminder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import main.App;
import main.entities.Reminder;
import main.notify.NotificationManager;

public class CreateReminderService {

	private CreateReminderService() {}

	private static final Logger logger = Logger.getLogger(CreateReminderService.class.getName());

	public static boolean execute(Reminder reminder) {
		logger.info("Executing CreateReminderService.");

		String sql = "INSERT INTO REMINDER (task_id, remind_at, cstm_message) VALUES (?, ?, ?)";

		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {

			pstm.setLong(1, reminder.task_id());
			pstm.setTimestamp(2, reminder.remind_at());
			pstm.setString(3, reminder.cstm_message());

			pstm.executeUpdate();

			logger.info("Reminder saved successfully.");
//			Start the reminder once it is saved
			NotificationManager nm = new NotificationManager();
			nm.scheduleReminder(reminder);
			
			return true;

		} catch (SQLException e) {
			logger.severe("Database connection error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
