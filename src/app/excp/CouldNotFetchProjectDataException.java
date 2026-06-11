package app.excp;

import app.enums.ExceptionMessages;

public class CouldNotFetchProjectDataException extends RuntimeException{
	public CouldNotFetchProjectDataException() {
		super(ExceptionMessages.PROJECT_DATA_FETCH_FAILED.getMessage());
	}
}
