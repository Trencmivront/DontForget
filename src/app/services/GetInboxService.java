package app.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import app.entities.Inbox;

public class GetInboxService {
	
	private GetInboxService() {}
	
	private static final Logger logger = Logger.getLogger(GetInboxService.class.getName());

	public static List<Inbox> execute(Connection conn) {
		logger.info("Class " + logger.getName() + " is executed.");
		
		String sql = "SELECT * FROM INBOX ORDER BY inbox_id DESC";
		List<Inbox> inboxList = new ArrayList<>();
		
		try (Statement stm = conn.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
			
			while (rs.next()) {
				int id = rs.getInt("inbox_id");
				String message = rs.getString("message");
				Timestamp ts = rs.getTimestamp("created_at");
				LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;
				
				inboxList.add(new Inbox(id, message, createdAt));
			}
			return inboxList;
			
		} catch (Exception e) {
			logger.warning("Error getting inbox records: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
