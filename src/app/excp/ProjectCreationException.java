package app.excp;

import app.enums.ExceptionMessages;

public class ProjectCreationException extends RuntimeException{
	public ProjectCreationException () {
		super(ExceptionMessages.PROJECT_CREATION_FAILED.getMessage());
	}
}
