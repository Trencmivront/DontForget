package app.excp;

import app.enums.ExceptionMessages;

public class DatabaseConnectionException  extends RuntimeException{
	public DatabaseConnectionException() {
		super(ExceptionMessages.DATABASE_CONNECTION_FAILED.getMessage());
	}
}
