package app.excp;

import app.enums.ExceptionMessages;

public class CouldNotFetchIconColorDataException extends RuntimeException{
	
	public CouldNotFetchIconColorDataException() {
		super(ExceptionMessages.ICON_COLOR_DATA_FETCH_FAILED.getMessage());
	}

}
