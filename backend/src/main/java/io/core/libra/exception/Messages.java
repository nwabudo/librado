package io.core.libra.exception;

import io.core.libra.dtos.ApiResponse;

public enum Messages {
	
	MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields."),
	USER_RECORD_ALREADY_EXISTS("User Already Exists"),
	BOOK_RECORD_ALREADY_EXISTS("Either Book Already Exists for the User or User Limit Reached"),
	BOOK_RECORD_DOES_NOT_EXISTS("Book with Provided ISBN Code not Found for the User"),
	PROBLEM_BORROWING_BOOK("Problem borrowing book"),
	SUCCESS_BORROWING_BOOK("Success! Book added to User's Catalogue"),
	SUCCESS_RETURNING_BOOK("Success! Book has been returned by User"),
	SUCCESS("Operation was a Success"),
	INTERNAL_SERVER_ERROR("An Error has Occurred"),
	NO_USER_RECORD_FOUND("User Record with Provided Id not Found"),
	NO_BOOK_RECORD_FOUND("Book with Provided ISBN Code not Found"),
	INVALID_EMAIL("must be a well-formed email address"),
	VALIDATION_ERRORS("Validation Errors"),
	ERROR_WRITING_JSON_RESPONSE("Error writing JSON output"),
	MEDIA_TYPE_NOT_SUPPORTED("Media type is not supported. Supported media types are "),
	METHOD_NOT_SUPPORTED("Method not Supported, Supported Method are:"),
	MALFORMED_JSON_REQUEST("Malformed JSON Request"),
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
