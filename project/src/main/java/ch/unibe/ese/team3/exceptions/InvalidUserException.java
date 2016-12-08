package ch.unibe.ese.team3.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8109094456523252807L;

}
