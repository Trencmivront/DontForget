package app.services.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import app.App;
import app.entities.Task;

public class GetTasksOfProjectService {
 private GetTasksOfProjectService() {}
	private static final Logger logger = Logger.getLogger(GetTasksOfProjectService.class.getName());
	
	public static List<Task> execute(int id){
		logger.info("Service executed.");
		
		String sql = "SELECT * FROM TASK WHERE project_id = ?";
		List<Task> tasks = new ArrayList<Task>();
		
		try (PreparedStatement pstm = App.connection.prepareStatement(sql)){
			
			pstm.setInt(1, id);
			ResultSet rs = pstm.executeQuery();
						
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
			JOptionPane.showMessageDialog(new JDialog(), "Error while getting tasks for project id="+id);
			e.printStackTrace();
		}
		return null;	
	}
}
