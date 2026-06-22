package main.services.reminder;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;
import main.notify.NotificationManager;
import main.services.recurring.DeleteRecurringTaskService;

public class DeleteReminderService {

	private DeleteReminderService() {}

	private static final Logger logger = Logger.getLogger(DeleteReminderService.class.getName());

	public static boolean execute(int id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		String sql = "DELETE FROM REMINDER WHERE task_id = ?";
		try (PreparedStatement pstm = App.connection.prepareStatement(sql)) {
			pstm.setInt(1, id);
//			we need to delete recurring task and it's connections first
			DeleteRecurringTaskService.execute(id);
			pstm.executeUpdate();
			logger.info("Reminder deleted.");
			
//			Also delete the reminder from here
			NotificationManager nm = new NotificationManager();
			nm.cancelReminder(id);
			
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting reminder for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
