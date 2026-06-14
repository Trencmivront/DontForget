package app.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import app.entities.Task;

public class GetTasksOfProjectService {
 private GetTasksOfProjectService() {}
	private static final Logger logger = Logger.getLogger(GetTasksOfProjectService.class.getName());
	
	public static List<Task> execute(int id, Connection conn){
		logger.info("");
		
		String sql = "SELECT * FROM TASK WHERE project_id = ?";
		
		try (PreparedStatement pstm = conn.prepareStatement(sql)){
			
			pstm.setInt(1, id);
			ResultSet rs = pstm.executeQuery();
			
			List<Task> tasks = new ArrayList<Task>();
			
			while(rs.next()) {
				tasks.add(new Task(
						rs.getInt("task_id"),
						rs.getString("task_title"),
						rs.getString("description"),
						rs.getInt("status_id"),
						rs.getInt("priority"),
						rs.getDate("due_date"),
						rs.getInt("list_order"),
						rs.getInt("project_id"),
						rs.getDate("created_at"),
						rs.getDate("updated_at"),
						rs.getDate("completed_at")));
			}
			
			return tasks;
			
		}catch (Exception e) {
			JOptionPane.showMessageDialog(new JDialog(), "Error while getting tasks for project id="+id);
			e.printStackTrace();
			return null;
		}
		
	}

}
