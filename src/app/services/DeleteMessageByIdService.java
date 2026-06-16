package app.services;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

import app.App;

public class DeleteMessageByIdService {
	
	private DeleteMessageByIdService() {}
	
	private static final Logger logger = Logger.getLogger(DeleteMessageByIdService.class.getName());

	public static boolean execute(int id) {
		logger.info("Class " + logger.getName() + " is executed with input id:" + id);
		
		String sql = "DELETE FROM INBOX WHERE inbox_id = ?";
		
		try (PreparedStatement pstm = App.connection.prepareStatement(sql)) {
			pstm.setInt(1, id);
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
