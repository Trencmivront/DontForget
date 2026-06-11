package app.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import app.entities.Project;
import app.excp.CouldNotFetchProjectDataException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GetProjectsService {
	
	private static final Logger logger = Logger.getLogger(GetProjectsService.class.getName());

	public static List<Project> execute(Connection conn){
		logger.info("The " + GetProjectsService.class.getName() + " has been executed.");
		try(Statement stm = conn.createStatement()) {
			
			String sql = "SELECT * FROM PROJECTS";
			
			List<Project> projects = new ArrayList<Project>();
			
			ResultSet rs = stm.executeQuery(sql);
			
			while(rs.next()) {
				projects.add(new Project(
						rs.getInt("project_id"),
						rs.getString("project_title"),
						rs.getInt("list_order"),
						rs.getInt("icon_color_id")
						));
			}
			return projects;
			
		}catch (SQLException e) {
			throw new CouldNotFetchProjectDataException();
		}
	}
	
	
}
