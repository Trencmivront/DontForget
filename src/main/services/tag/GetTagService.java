package main.services.tag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import main.App;
import main.entities.Tag;

public class GetTagService {

	private GetTagService() {}

	private static final Logger logger = Logger.getLogger(GetTagService.class.getName());

	public static Tag execute(int tagId) {
		logger.info("Executing GetTagService with tagId: " + tagId);

		String sql = "SELECT * FROM TAG WHERE tag_id = ?";

		try (PreparedStatement pstm = App.connection.prepareStatement(sql)) {
			pstm.setInt(1, tagId);

			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					return new Tag(
						rs.getInt("tag_id"),
						rs.getString("tag_name"),
						rs.getInt("icon_color_id")
					);
				}
			}
		} catch (SQLException e) {
			logger.warning("Error fetching tag with ID " + tagId + ": " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
}
