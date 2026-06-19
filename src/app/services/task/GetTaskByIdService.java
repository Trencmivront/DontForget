package app.services.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import app.App;
import app.entities.Task;

public class GetTaskByIdService {
	
	private static final Logger logger = Logger.getLogger(GetTaskByIdService.class.getName());
	
	public Task execute(int id) {
		
		String sql = "SELECT * FROM TASK WHERE task_id=?";
		
		try (PreparedStatement pstm = App.connection.prepareStatement(sql)){
			pstm.setInt(1, id);
			
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {
				return new Task(rs.getInt("task_id"),
						rs.getString("task_title"),
						rs.getString("description"),
						rs.getInt("status_id"),
						rs.getInt("priority"),
						rs.getTimestamp("due_date"),
						rs.getInt("list_order"),
						rs.getInt("project_id"),
						rs.getTimestamp("created_at"),
						rs.getTimestamp("updated_at"),
						rs.getTimestamp("completed_at"));
			}
			
		}catch (SQLException e) {
			logger.warning("SQLException occoured: " + e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			logger.warning("An exception occoured: " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
		
	}

}
