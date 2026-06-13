package app.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.entities.IconColor;

public class GetIconColorsService {
	
	public static List<IconColor> execute(Connection conn){
		
		try (Statement stm = conn.createStatement()){
			
			String sql = "SELECT * FROM ICON_COLOR";
			
			stm.execute(sql);
			
			ResultSet rs = stm.getResultSet();
			
			List<IconColor> ic = new ArrayList<>();
			
			while(rs.next()) {
				ic.add(new IconColor(rs.getInt("icon_color_id"),
						rs.getInt("red"),
						rs.getInt("green"),
						rs.getInt("blue")));
			}
			
			return ic;
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
