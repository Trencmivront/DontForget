package main.services.task;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Logger;

import main.App;
import main.dco.TaskDCO;

public class CreateTaskService {

	private static final Logger logger = Logger.getLogger(CreateTaskService.class.getName());

	private CreateTaskService() {}

	public static int execute(TaskDCO task) {
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
			e.printStackTrace();
		}

		String insertTaskSql = "INSERT INTO TASK (task_title, description, status_id, priority, due_date, list_order, project_id, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

		try (PreparedStatement pstm = App.connection.prepareStatement(insertTaskSql, Statement.RETURN_GENERATED_KEYS)){

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

				pstm.execute();

				logger.info("Task saved successfully.");
				ResultSet rs = pstm.getGeneratedKeys();
				if(rs.next()) {
					return rs.getInt(1);
				}
				return 0;

		} catch (SQLException e) {
			logger.severe("Database connection error: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}
}
