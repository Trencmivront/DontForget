package main.services.inbox;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import main.App;

public class DeleteMessageByIdService {
	
	
	private static final Logger logger = Logger.getLogger(DeleteMessageByIdService.class.getName());

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id:" + id);
		
		String sql = "DELETE FROM INBOX WHERE inbox_id = ?";
		
		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {
			pstm.setLong(1, id);
			int rowsAffected = pstm.executeUpdate();
			logger.info("Message deleted.");
			return rowsAffected > 0;
		} catch (Exception e) {
			logger.warning("Error deleting inbox message with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
