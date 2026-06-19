
package app.services.reminder;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import app.App;
import app.entities.Reminder;

public class GetRemindersService {
	
	private GetRemindersService() {}
	
	private static final Logger logger = Logger.getLogger(GetRemindersService.class.getName());

	public static List<Reminder> execute() {
		logger.info("Class " + logger.getName() + " is executed.");
		
		String sql = "SELECT * FROM REMINDER ORDER BY remind_at ASC";
		List<Reminder> reminders = new ArrayList<>();
		
		try (Statement stm = App.connection.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
			
			while (rs.next()) {
				reminders.add(new Reminder(
						rs.getLong("task_id"),
						rs.getTimestamp("remind_at"),
						rs.getString("cstm_message")));
			}
			return reminders;
			
		} catch (Exception e) {
			logger.warning("Error getting reminder records: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
