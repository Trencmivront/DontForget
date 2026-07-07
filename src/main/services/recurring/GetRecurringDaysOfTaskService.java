package main.services.recurring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.App;

public class GetRecurringDaysOfTaskService {

	private static final Logger logger = Logger.getLogger(GetRecurringDaysOfTaskService.class.getName());

	private GetRecurringDaysOfTaskService() {}

	public static List<DayOfWeek> execute(int taskId) {
		logger.info("Executing GetRecurringDaysOfTaskService for taskId: " + taskId);
		List<DayOfWeek> days = new ArrayList<>();
		String sql = "SELECT week_day_id FROM RECURRING_TASK WHERE task_id = ?";

		try (PreparedStatement pstm = App.connection.prepareStatement(sql)) {
			pstm.setInt(1, taskId);
			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					int dayValue = rs.getInt("week_day_id");
					days.add(DayOfWeek.of(dayValue));
				}
			}
		} catch (SQLException e) {
			logger.severe("Error retrieving recurring days: " + e.getMessage());
			e.printStackTrace();
		}
		return days;
	}
}
