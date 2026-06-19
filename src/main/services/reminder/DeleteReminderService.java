package main.services.reminder;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;

public class DeleteReminderService {

	private DeleteReminderService() {}

	private static final Logger logger = Logger.getLogger(DeleteReminderService.class.getName());

	public static boolean execute(int id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		String sql = "DELETE FROM REMINDER WHERE task_id = ?";

		try (PreparedStatement pstm = App.connection.prepareStatement(sql)) {
			pstm.setInt(1, id);
			pstm.executeUpdate();
			logger.info("Reminder deleted.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting reminder for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
