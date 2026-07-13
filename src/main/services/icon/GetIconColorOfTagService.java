package main.services.icon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.App;
import main.entities.IconColor;

public class GetIconColorOfTagService {
	
	public IconColor execute(Long id) {
		
		String sql = "SELECT * FROM ICON_COLOR ic JOIN TAG t ON "
				+ "t.icon_color_id=ic.icon_color_id WHERE t.tag_id=(?)";
		
		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)){
			
			pstm.setLong(1, id);

			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				return new IconColor(rs.getLong("icon_color_id"),
						rs.getInt("red"),
						rs.getInt("green"),
						rs.getInt("blue"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

}
