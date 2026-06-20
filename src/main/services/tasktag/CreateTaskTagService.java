package main.services.tasktag;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import main.App;
import main.entities.TaskTag;

public class CreateTaskTagService {

	private CreateTaskTagService() {}

	private static final Logger logger = Logger.getLogger(CreateTaskTagService.class.getName());

	public static boolean execute(TaskTag taskTag) {
		logger.info("Executing CreateTaskTagService.");

		String sql = "INSERT INTO TASK_TAG (task_id, tag_id) VALUES (?, ?)";

		try (PreparedStatement pstm = App.connection.prepareStatement(sql)) {

			pstm.setInt(1, taskTag.task_id());
			pstm.setInt(2, taskTag.tag_id());

			pstm.executeUpdate();

			logger.info("TaskTag saved successfully.");
			return true;

		} catch (SQLException e) {
			logger.severe("Database connection error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
