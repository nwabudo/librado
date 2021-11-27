package io.core.libra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = 3550684939939471934L;

	public UserServiceException(String message) {
		super(message);
	}
}
