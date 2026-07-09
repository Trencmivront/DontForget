package main.services.reminder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import main.App;
import main.entities.Reminder;

public class GetReminderByIdService {

	private GetReminderByIdService() {}

	private static final Logger logger = Logger.getLogger(GetReminderByIdService.class.getName());

	public static Reminder execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with id: " + id);

		String sql = "SELECT * FROM REMINDER WHERE task_id = ?";

		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {
			pstm.setLong(1, id);
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					return new Reminder(
							rs.getLong("task_id"),
							rs.getTimestamp("remind_at"),
							rs.getString("cstm_message")
					);
				}
			}
		} catch (Exception e) {
			logger.warning("Error getting reminder for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
