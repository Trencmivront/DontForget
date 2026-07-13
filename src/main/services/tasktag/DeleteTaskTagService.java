package main.services.tasktag;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;

public class DeleteTaskTagService {


	private static final Logger logger = Logger.getLogger(DeleteTaskTagService.class.getName());

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		String sql = "DELETE FROM TASK_TAG WHERE task_id = ?";

		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {
			pstm.setLong(1, id);
			pstm.executeUpdate();
			logger.info("Task tags deleted.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting task tags for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
