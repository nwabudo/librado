package io.core.libra.exception;

public enum Messages {
	
	MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields."),
	BOOK_RECORD_ALREADY_EXISTS("Either Book Already Exists for the User or User Limit Reached"),
	BOOK_RECORD_DOES_NOT_EXISTS("Book with Provided ISBN Code not Found for the User"),
	PROBLEM_BORROWING_BOOK("Problem borrowing book"),
	SUCCESS_BORROWING_BOOK("Success! Book added to User's Catalogue"),
	SUCCESS_RETURNING_BOOK("Success! Book has been returned by User"),
	SUCCESS("Operation was a Success"),
	INTERNAL_SERVER_ERROR("An Error has Occurred"),
	NO_USER_RECORD_FOUND("User Record with Provided Id not Found"),
	NO_BOOK_RECORD_FOUND("Book with Provided ISBN Code not Found"),
	COULD_NOT_UPDATE_RECORD("Could not Update record"),
	COULD_NOT_DELETE_RECORD("Could not Delete record"),
	COULD_NOT_INSERT_RECORD("Could not Insert record"),
	NOT_EQUAL("Record is not Equal");

    private String message;
	
	Messages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
