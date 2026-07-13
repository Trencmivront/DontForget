package main.services.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.App;
import main.services.task.DeleteTaskService;

public class DeleteProjectService {


	private static final Logger logger = Logger.getLogger(DeleteProjectService.class.getName());

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		// 1. Fetch all task_ids of this project
		String fetchTasksSql = "SELECT task_id FROM TASK WHERE project_id = ?";
		List<Long> taskIds = new ArrayList<>();

		try (PreparedStatement pstmFetch = App.getConnection().prepareStatement(fetchTasksSql)) {
			pstmFetch.setLong(1, id);
			try (ResultSet rs = pstmFetch.executeQuery()) {
				while (rs.next()) {
					taskIds.add(rs.getLong("task_id"));
				}
			}
		} catch (Exception e) {
			logger.warning("Error fetching tasks for project ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		// 2. Delete each task using DeleteTaskService
		for (Long taskId : taskIds) {
			boolean taskDeleted = new DeleteTaskService().execute(taskId);
			if (!taskDeleted) {
				logger.warning("Failed to delete task with ID: " + taskId);
			}
		}

		// 3. Delete the project itself
		String deleteProjectSql = "DELETE FROM PROJECT WHERE project_id = ?";
		try (PreparedStatement pstmDelProject = App.getConnection().prepareStatement(deleteProjectSql)) {
			pstmDelProject.setLong(1, id);
			int rowsAffected = pstmDelProject.executeUpdate();
			logger.info("Project deleted successfully.");
			return rowsAffected > 0;
		} catch (Exception e) {
			logger.warning("Error deleting project with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
