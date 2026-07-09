package main.services.icon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import main.App;
import main.entities.IconColor;

public class GetIconColorOfProjectService {
	
	private static final Logger logger = Logger.getLogger(GetIconColorOfProjectService.class.getName());
	
	public static IconColor execute(Long id) {
		
		logger.info("The class " + logger.getName() + " is executed.");
		
		try {
			
			String sql = "SELECT * FROM ICON_COLOR ic JOIN PROJECT p "
					+ "ON p.icon_color_id = ic.icon_color_id WHERE p.project_id = (?)";
			
			try (PreparedStatement stm = App.getConnection().prepareStatement(sql)) {
				stm.setLong(1, id);
				try (ResultSet rs = stm.executeQuery()) {
					if(rs.next()) {
						return new IconColor(rs.getLong("icon_color_id"),
								rs.getInt("red"),
								rs.getInt("green"),
								rs.getInt("blue"));
					}
				}
			}
			
			return null;
		} catch (SQLException s) {
			s.printStackTrace();
			return null;
		}
		
	}

}
