package org.gegma.exceptions;

import java.io.IOException;

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
