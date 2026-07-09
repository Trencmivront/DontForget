package main.services.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import main.App;
import main.dco.ProjectDCO;

public class CreateProjectService {
	
	private static final Logger logger = Logger.getLogger(CreateProjectService.class.getName());

	public static boolean execute(ProjectDCO p){
		logger.info("Class " + logger.getName() + " is executed.");
		
		String createSql = "INSERT INTO PROJECT (project_title, description, list_order, icon_color_id)"
				+ " VALUES (?, ?, ?, ?)";
		
		String listOrderSql = "SELECT MAX(list_order) as list_order FROM PROJECT";
		
		try(PreparedStatement pstm = App.getConnection().prepareStatement(createSql);
				Statement stm = App.getConnection().createStatement()) {
			
			stm.execute(listOrderSql);
			
			ResultSet rs = stm.getResultSet();
			int listOrder = 0;
			if(rs.next()) {
				listOrder = rs.getInt("list_order");
			}
			// increase list order for this project to append it at last
			listOrder++;
			
			pstm.setString(1, p.project_title());
			pstm.setString(2, p.description());
			pstm.setInt(3, listOrder);
			pstm.setLong(4, p.icon_color_id());
			
			//if the result is empty
			return !pstm.execute();
				
		}catch (Exception e) {
			logger.warning(e.getClass().getName() + " thrown." );
			e.printStackTrace();
			return false;
		}
	}

}
