package io.core.libra.exception;

public enum Messages {
	
	MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields."),
	BOOK_RECORD_ALREADY_EXISTS("Book Already Exists for the User"),
	PROBLEM_BORROWING_BOOK("Problem borrowing book"),
	SUCCESS_BORROWING_BOOK("Success!! Book added to User"),
	INTERNAL_SERVER_ERROR("An Error has Occurred"),
	NO_USER_RECORD_FOUND("User Record with Provided Id not Found"),
	NO_BOOK_RECORD_FOUND("Book with Provided ISBN Code not Found"),
	COULD_NOT_UPDATE_RECORD("Could not Update record"),
	COULD_NOT_DELETE_RECORD("Could not Delete record"),
	COULD_NOT_INSERT_RECORD("Could not Insert record"),
	NOT_EQUAL("Record is not Equal");

    private String errorMessage;
	
	Messages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
