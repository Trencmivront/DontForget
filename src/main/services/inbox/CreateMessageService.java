package main.services.inbox;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import main.App;

public class CreateMessageService {


	private static final Logger logger = Logger.getLogger(CreateMessageService.class.getName());

	public boolean execute(String message) {
		logger.info("Executing CreateMessageService.");

		String sql = "INSERT INTO INBOX (message) VALUES (?)";

		try (PreparedStatement pstm = App.getConnection().prepareStatement(sql)) {
			pstm.setString(1, message);
			int rowsAffected = pstm.executeUpdate();
			logger.info("Message saved successfully.");
			return rowsAffected > 0;
		} catch (SQLException e) {
			logger.severe("Database connection error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
