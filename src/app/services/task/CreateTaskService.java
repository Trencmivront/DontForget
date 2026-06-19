package app.services.task;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Logger;

import app.App;
import app.dco.TaskDCO;

public class CreateTaskService {

	private static final Logger logger = Logger.getLogger(CreateTaskService.class.getName());

	private CreateTaskService() {}

	public static boolean execute(TaskDCO task, Timestamp remindAt, String reminderMsg) {
		logger.info("Executing CreateTaskService.");

		int projectId = task.project_id();
		int listOrder = 1;
		String maxOrderSql = "SELECT MAX(list_order) as max_order FROM TASK WHERE project_id = ?";
		try (PreparedStatement pstm = App.connection.prepareStatement(maxOrderSql)) {
			pstm.setInt(1, projectId);
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					listOrder = rs.getInt("max_order") + 1;
				}
			}
		} catch (Exception e) {
			logger.warning("Error getting max list_order: " + e.getMessage());
		}

		String insertTaskSql = "INSERT INTO TASK (task_title, description, status_id, priority, due_date, list_order, project_id, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

		try {
			App.connection.setAutoCommit(false);

			try (PreparedStatement pstm = App.connection.prepareStatement(insertTaskSql,
					Statement.RETURN_GENERATED_KEYS)) {
				pstm.setString(1, task.task_title());
				pstm.setString(2, task.description() == null || task.description().isEmpty() ? null : task.description());
				pstm.setInt(3, task.status_id() != null ? task.status_id() : 1); // 1 = ACTIVE

				if (task.priority() != null) {
					pstm.setInt(4, task.priority());
				} else {
					pstm.setNull(4, Types.INTEGER);
				}

				if (task.due_date() != null) {
					pstm.setDate(5, Date.valueOf(task.due_date()));
				} else {
					pstm.setNull(5, Types.DATE);
				}

				pstm.setInt(6, listOrder);
				pstm.setInt(7, projectId);

				pstm.executeUpdate();

				int taskId = -1;
				try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						taskId = generatedKeys.getInt(1);
					}
				}

				if (taskId == -1) {
					throw new SQLException("Failed to retrieve generated task_id.");
				}

				if (remindAt != null) {
					String insertReminderSql = "INSERT INTO REMINDER (task_id, remind_at, cstm_message) VALUES (?, ?, ?)";
					try (PreparedStatement rpstm = App.connection.prepareStatement(insertReminderSql)) {
						rpstm.setInt(1, taskId);
						rpstm.setTimestamp(2, remindAt);
						rpstm.setString(3, reminderMsg);
						rpstm.executeUpdate();
					}
				}

				App.connection.commit();
				logger.info("Task and reminders saved successfully.");
				return true;

			} catch (Exception ex) {
				App.connection.rollback();
				logger.severe("Transaction rolled back due to error: " + ex.getMessage());
				ex.printStackTrace();
				return false;
			} finally {
				App.connection.setAutoCommit(true);
			}
		} catch (Exception e) {
			logger.severe("Database connection error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
