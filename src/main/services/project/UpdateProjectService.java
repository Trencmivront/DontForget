package main.services.project;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import main.App;
import main.dco.ProjectDCO;

public class UpdateProjectService {

	private static final Logger logger = Logger.getLogger(UpdateProjectService.class.getName());


	public boolean execute(ProjectDCO p, Long id) {
		logger.info("Class " + logger.getName() + " is executed with project ID: " + id);

		if (p == null) {
			logger.warning("ProjectDCO is null. Aborting update.");
			return false;
		}

		String sql = "UPDATE PROJECT SET project_title = ?, description = ?, icon_color_id = ? WHERE project_id = ?";

		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {
			pstm.setString(1, p.project_title());
			pstm.setString(2, p.description() == null || p.description().isEmpty() ? null : p.description());
			pstm.setLong(3, p.icon_color_id());
			pstm.setLong(4, id);

			int rowsAffected = pstm.executeUpdate();
			logger.info("Project update complete. Rows affected: " + rowsAffected);
			return rowsAffected > 0;
		} catch (SQLException e) {
			logger.severe("SQLException occurred: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
