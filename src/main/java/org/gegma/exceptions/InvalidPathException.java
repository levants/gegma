package org.gegma.exceptions;

import java.io.IOException;

import org.gegma.Place;

/**
 * {@link Exception} thrown if {@link Place} has not any valid outputs
 * 
 * @author levan
 * 
 */
public class InvalidPathException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InvalidPathException() {
	super();
    }

    public InvalidPathException(String message) {
	super(message);
    }

    public InvalidPathException(String message, Throwable cause) {
	super(message, cause);
    }
}
