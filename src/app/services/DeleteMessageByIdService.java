package app.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

public class DeleteMessageByIdService {
	
	private DeleteMessageByIdService() {}
	
	private static final Logger logger = Logger.getLogger(DeleteMessageByIdService.class.getName());

	public static boolean execute(Connection conn, int id) {
		logger.info("Class " + logger.getName() + " is executed with input id:" + id);
		
		String sql = "DELETE FROM INBOX WHERE inbox_id = ?";
		
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, id);
			int rowsAffected = pstm.executeUpdate();
			return rowsAffected > 0;
		} catch (Exception e) {
			logger.warning("Error deleting inbox message with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
