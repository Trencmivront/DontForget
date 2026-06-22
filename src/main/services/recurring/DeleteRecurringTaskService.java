package main.services.recurring;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;

public class DeleteRecurringTaskService {

	private DeleteRecurringTaskService() {}

	private static final Logger logger = Logger.getLogger(DeleteRecurringTaskService.class.getName());

	public static boolean execute(int id) {
		logger.info(String.format("Class %s is executed with input id: %d", logger.getName(), id));
//		We need to delete relationships first
		String relationSql = "DELETE FROM RECURRING_TASK_WEEK_DAYS WHERE task_id = ?";
		String sql = "DELETE FROM RECURRING_TASK WHERE task_id = ?";
		
		try (PreparedStatement pstm = App.connection.prepareStatement(sql);
				PreparedStatement pstm2 = App.connection.prepareStatement(relationSql)) {
			pstm2.setInt(1, id);
			if(pstm2.executeUpdate() != 0) {
				
			}
			
			pstm.setInt(1, id);
			if(pstm.executeUpdate() != 0) {
				logger.info("Recurring task deleted.");
			}
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting recurring task for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
