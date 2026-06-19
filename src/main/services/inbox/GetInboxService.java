package main.services.inbox;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.App;
import main.entities.Inbox;

public class GetInboxService {
	
	private GetInboxService() {}
	
	private static final Logger logger = Logger.getLogger(GetInboxService.class.getName());

	public static List<Inbox> execute() {
		logger.info("Class " + logger.getName() + " is executed.");
		
		String sql = "SELECT * FROM INBOX ORDER BY inbox_id DESC";
		List<Inbox> inboxList = new ArrayList<>();
		
		try (Statement stm = App.connection.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
			
			while (rs.next()) {
				inboxList.add(
						new Inbox(
						rs.getLong("inbox_id"),
						rs.getString("message"),
						rs.getTimestamp("created_at")));
			}
			return inboxList;
			
		} catch (Exception e) {
			logger.warning("Error getting inbox records: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
