package main.services.icon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.App;
import main.entities.IconColor;

public class GetIconColorsService {
 private GetIconColorsService() {}

	
	public static List<IconColor> execute(){
		
		try (Statement stm = App.getConnection().createStatement()){
			
			String sql = "SELECT * FROM ICON_COLOR";
			
			stm.execute(sql);
			
			ResultSet rs = stm.getResultSet();
			
			List<IconColor> ic = new ArrayList<>();
			
			while(rs.next()) {
				ic.add(new IconColor(rs.getLong("icon_color_id"),
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
