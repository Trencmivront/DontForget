package main.services.task;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Logger;

import main.App;
import main.entities.Task;

public class UpdateTaskService {

	private static final Logger logger = Logger.getLogger(UpdateTaskService.class.getName());

	private UpdateTaskService() {}

	public static boolean execute(Task task) {
		logger.info("Class " + logger.getName() + " is executed with task ID: " + (task != null ? task.task_id() : "null"));

		if (task == null || task.task_id() == null) {
			logger.warning("Task or Task ID is null. Aborting update.");
			return false;
		}

		String sql = "UPDATE TASK SET task_title = ?, description = ?, status_id = ?, priority = ?, due_date = ?, list_order = ?, project_id = ?, updated_at = CURRENT_TIMESTAMP, completed_at = ? WHERE task_id = ?";

		try (PreparedStatement pstm = App.connection.prepareStatement(sql)) {
			pstm.setString(1, task.task_title());
			pstm.setString(2, task.description() == null || task.description().isEmpty() ? null : task.description());
			pstm.setInt(3, task.status_id() != null ? task.status_id() : 1);

			if (task.priority() != 0) {
				pstm.setInt(4, task.priority());
			} else {
				pstm.setNull(4, Types.INTEGER);
			}

			if (task.due_date() != null) {
				pstm.setTimestamp(5, task.due_date());
			} else {
				pstm.setNull(5, Types.TIMESTAMP);
			}

			if (task.list_order() != 0) {
				pstm.setInt(6, task.list_order());
			} else {
				pstm.setNull(6, Types.INTEGER);
			}

			if (task.project_id() != 0) {
				pstm.setInt(7, task.project_id());
			} else {
				pstm.setNull(7, Types.INTEGER);
			}

			// Handle completed_at timestamp:
			// If status is COMPLETED (2), set completed_at (either provided or current time).
			// Otherwise (ACTIVE or other), reset completed_at to null.
			if (task.status_id() != 0 && task.status_id() == 2) {
				if (task.completed_at() != null) {
					pstm.setTimestamp(8, task.completed_at());
				} else {
					pstm.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
				}
			} else {
				pstm.setNull(8, Types.TIMESTAMP);
			}

			pstm.setInt(9, task.task_id());

			int rowsAffected = pstm.executeUpdate();
			logger.info("Task update complete. Rows affected: " + rowsAffected);
			return rowsAffected > 0;
		} catch (SQLException e) {
			logger.severe("SQLException occurred: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
