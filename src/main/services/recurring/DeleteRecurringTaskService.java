package main.services.recurring;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;

public class DeleteRecurringTaskService {


	private static final Logger logger = Logger.getLogger(DeleteRecurringTaskService.class.getName());

	public boolean execute(Long id) {
		logger.info(String.format("Class %s is executed with input id: %d", logger.getName(), id));
		String sql = "DELETE FROM RECURRING_TASK WHERE task_id = ?";
		
		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {
			pstm.setLong(1, id);
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
