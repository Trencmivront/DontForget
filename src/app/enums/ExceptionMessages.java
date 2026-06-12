package app.enums;

/**
 * Global exception messages in a single place.
 */
public enum ExceptionMessages {
	
	DATABASE_INITIALIZATION_FAILED("Failed to initialize the database."),
	DATABASE_CONNECTION_FAILED("Failed to connect database."),
	PROJECT_DATA_FETCH_FAILED("Could not retrieve project data."),
	ICON_COLOR_DATA_FETCH_FAILED("Could not retrieve icon color data."),
	PROJECT_CREATION_FAILED("Could not create the project.");
	
	private final String message;

	ExceptionMessages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
