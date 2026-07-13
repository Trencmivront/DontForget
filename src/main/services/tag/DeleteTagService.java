package main.services.tag;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;

public class DeleteTagService {


	private static final Logger logger = Logger.getLogger(DeleteTagService.class.getName());

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		// 1. Delete references in the junction table TASK_TAG
		String deleteJunctionSql = "DELETE FROM TASK_TAG WHERE tag_id = ?";
		try (PreparedStatement pstmJunction = App.getConnection().prepareStatement(deleteJunctionSql)) {
			pstmJunction.setLong(1, id);
			pstmJunction.executeUpdate();
		} catch (Exception e) {
			logger.warning("Error deleting TASK_TAG references for tag ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		// 2. Delete the tag itself
		String deleteTagSql = "DELETE FROM TAG WHERE tag_id = ?";
		try (PreparedStatement pstmDelTag = App.getConnection().prepareStatement(deleteTagSql)) {
			pstmDelTag.setLong(1, id);
			int rowsAffected = pstmDelTag.executeUpdate();
			logger.info("Tag deleted successfully.");
			return rowsAffected > 0;
		} catch (Exception e) {
			logger.warning("Error deleting tag with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
