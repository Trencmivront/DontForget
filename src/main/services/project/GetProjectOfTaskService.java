package main.services.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import main.App;
import main.entities.Project;

public class GetProjectOfTaskService {

	private GetProjectOfTaskService() {}

	private static final Logger logger = Logger.getLogger(GetProjectOfTaskService.class.getName());

	public static Project execute(Long taskId) {
		logger.info("Class " + logger.getName() + " is executed with task ID: " + taskId);

		String sql = "SELECT P.* FROM PROJECT P JOIN TASK T ON P.project_id = T.project_id WHERE T.task_id = ?";

		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {
			pstm.setLong(1, taskId);

			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					return new Project(
							rs.getLong("project_id"),
							rs.getString("project_title"),
							rs.getString("description"),
							rs.getInt("list_order"),
							rs.getLong("icon_color_id")
					);
				}
			}
		} catch (SQLException e) {
			logger.severe("SQLException occurred: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.severe("An exception occurred: " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
}
