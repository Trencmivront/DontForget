package app.excp;

import app.enums.ExceptionMessages;

public class DatabaseInitializationException extends RuntimeException {
	public DatabaseInitializationException() {
		super(ExceptionMessages.DATABASE_INITIALIZATION_FAILED.getMessage());
	}

}
