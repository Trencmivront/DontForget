package main.services.tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.App;
import main.entities.Tag;

public class GetTagsService {
	
	public static List<Tag> execute(){
		
		try (Statement stm = App.connection.createStatement()){
			
			String sql = "SELECT * FROM TAG";
			
			ResultSet rs = stm.executeQuery(sql);
			
			List<Tag> tags = new ArrayList<Tag>();
			
			while(rs.next()) {
				tags.add(new Tag(rs.getInt("tag_id"),
						rs.getString("tag_name"),
						rs.getInt("icon_color_id")));
			}
			
			return tags.isEmpty() ? null : tags;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
	}

}
