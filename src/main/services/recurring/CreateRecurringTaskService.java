package main.services.recurring;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.logging.Logger;

import main.App;
import main.dco.RecurringTaskDCO;

public class CreateRecurringTaskService {

	private static final Logger logger = Logger.getLogger(CreateRecurringTaskService.class.getName());

	private CreateRecurringTaskService() {}

	public static boolean execute(int taskId, List<DayOfWeek> days) {
		logger.info("Executing CreateRecurringTaskService for taskId: " + taskId);

		String insertRecurringSql = "INSERT INTO RECURRING_TASK (task_id, week_day_id) VALUES (?, ?)";

		try (PreparedStatement pstm = App.connection.prepareStatement(insertRecurringSql)) {
			for (DayOfWeek day : days) {
				RecurringTaskDCO dco = new RecurringTaskDCO(taskId, day.getValue());
				pstm.setInt(1, dco.task_id());
				pstm.setInt(2, dco.week_day_id());
				pstm.executeUpdate();
			}
			logger.info("Recurring task created successfully for task: " + taskId);
			return true;
		} catch (SQLException e) {
			logger.severe("Error creating recurring task: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
