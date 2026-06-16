package app.services;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import app.App;
import app.entities.Project;

public class GetProjectsService {
	
	private GetProjectsService() {}
	
	private static final Logger logger = Logger.getLogger(GetProjectsService.class.getName());

	public static List<Project> execute(){
		logger.info("Class " + logger.getName() + " is executed.");
		try(Statement stm = App.connection.createStatement()) {
			
			String sql = "SELECT * FROM PROJECT ORDER BY list_order";
			
			List<Project> projects = new ArrayList<Project>();
			
			ResultSet rs = stm.executeQuery(sql);
			
			while(rs.next()) {
				projects.add(new Project(
						rs.getInt("project_id"),
						rs.getString("project_title"),
						rs.getString("description"),
						rs.getInt("list_order"),
						rs.getInt("icon_color_id")
						));
			}
			return projects;
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
