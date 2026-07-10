package main.services.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.App;
import main.entities.Task;

public class GetTasksService {

	public static List<Task> execute() {
		try (Statement stm = App.getConnection().createStatement()) {
			String sql = "SELECT * FROM TASK";
			ResultSet rs = stm.executeQuery(sql);
			List<Task> tasks = new ArrayList<>();
			while (rs.next()) {
				tasks.add(new Task(
						rs.getLong("task_id"),
						rs.getString("task_title"),
						rs.getString("description"),
						rs.getLong("status_id"),
						rs.getInt("priority"),
						rs.getTimestamp("due_date"),
						rs.getInt("list_order"),
						rs.getLong("project_id"),
						rs.getTimestamp("created_at"),
						rs.getTimestamp("updated_at"),
						rs.getTimestamp("completed_at")
				));
			}
			return tasks.isEmpty() ? null : tasks;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
