package app.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import app.entities.Project;
import app.excp.ProjectCreationException;

public class CreateProjectService {
	
	private static final Logger logger = Logger.getLogger(CreateProjectService.class.getName());

	public static boolean execute(Connection conn, Project p){
		logger.info("Class " + logger.getName() + " is executed.");
		
		String createSql = "INSERT INTO PROJECTS (project_title, description, list_order, icon_color_id)"
				+ " VALUES (?, ?, ?, ?)";
		
		String listOrderSql = "SELECT MAX(list_order) FROM PROJECTS";
		
		try(PreparedStatement pstm = conn.prepareStatement(createSql);
				Statement stm = conn.createStatement()) {
			
			stm.execute(listOrderSql);
			
			int listOrder = stm.getResultSet().getInt("list_order");
			// increase list order for this project to append it at last
			listOrder++;
			
			pstm.setString(1, p.project_title());
			pstm.setString(2, p.description());
			pstm.setInt(3, listOrder);
			pstm.setInt(4, p.icon_color_id());
			
			return pstm.execute();
			
		}catch (Exception e) {
			throw new ProjectCreationException();
		}
	}

}
