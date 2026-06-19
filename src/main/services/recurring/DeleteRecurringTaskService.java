package main.services.recurring;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;

public class DeleteRecurringTaskService {

	private DeleteRecurringTaskService() {}

	private static final Logger logger = Logger.getLogger(DeleteRecurringTaskService.class.getName());

	public static boolean execute(int id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		String sql = "DELETE FROM RECURRING_TASK WHERE task_id = ?";

		try (PreparedStatement pstm = App.connection.prepareStatement(sql)) {
			pstm.setInt(1, id);
			pstm.executeUpdate();
			logger.info("Recurring task deleted.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting recurring task for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
