package main.services.task;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.App;
import main.entities.Task;

public class GetTodaysTasksService {
 private GetTodaysTasksService() {}
	private static final Logger logger = Logger.getLogger(GetTodaysTasksService.class.getName());
	
	public static List<Task> execute(){
		logger.info("Service executed.");
				
		try {
			Statement pstm = App.connection.createStatement();
			
			String sql = "SELECT * FROM TASK WHERE "
					+ "FORMATDATETIME(due_date, '%Y-%m-%d') = FORMATDATETIME(NOW(), 'dd-MM-yyyy')";
			
			ResultSet rs = pstm.executeQuery(sql);
			
			List<Task> tasks = new ArrayList<Task>();
			
			while(rs.next()) {
				tasks.add(new Task(
						rs.getInt("task_id"),
						rs.getString("task_title"),
						rs.getString("description"),
						rs.getInt("status_id"),
						rs.getInt("priority"),
						rs.getTimestamp("due_date"),
						rs.getInt("list_order"),
						rs.getInt("project_id"),
						rs.getTimestamp("created_at"),
						rs.getTimestamp("updated_at"),
						rs.getTimestamp("completed_at")));
			}
			
			return tasks;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
	}
}
