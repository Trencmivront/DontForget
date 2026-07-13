package main.services.tasktag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.App;
import main.entities.TaskTag;

public class GetTaskTagByTaskService {


	private static final Logger logger = Logger.getLogger(GetTaskTagByTaskService.class.getName());

	public List<TaskTag> execute(Long taskId) {
		logger.info("Executing GetTaskTagByTaskService with taskId: " + taskId);

		String sql = "SELECT * FROM TASK_TAG WHERE task_id = ?";
		List<TaskTag> taskTags = new ArrayList<>();

		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {
			pstm.setLong(1, taskId);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					taskTags.add(new TaskTag(
						rs.getLong("task_id"),
						rs.getLong("tag_id")
					));
				}
			}
		} catch (SQLException e) {
			logger.warning("Error fetching task tags for task ID " + taskId + ": " + e.getMessage());
			e.printStackTrace();
		}

		return taskTags;
	}
}
