package main.services.task;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;
import main.services.reminder.DeleteReminderService;
import main.services.tasktag.DeleteTaskTagService;

public class DeleteTaskService {

	private DeleteTaskService() {}

	private static final Logger logger = Logger.getLogger(DeleteTaskService.class.getName());

	public static boolean execute(int id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		// 1. Delete associated connections using the specialized service classes
		DeleteReminderService.execute(id);
		DeleteTaskTagService.execute(id);

		// 2. Delete the task record itself
		String deleteTaskSql = "DELETE FROM TASK WHERE task_id = ?";

		try (PreparedStatement pstmTask = App.connection.prepareStatement(deleteTaskSql)) {
			pstmTask.setInt(1, id);
			int rowsAffected = pstmTask.executeUpdate();
			logger.info("Task deleted. Rows affected: " + rowsAffected);
			return rowsAffected > 0;
		} catch (Exception e) {
			logger.warning("Error deleting task with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
