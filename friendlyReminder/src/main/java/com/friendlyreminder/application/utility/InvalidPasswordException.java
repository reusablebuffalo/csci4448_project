package com.friendlyreminder.application.utility;

/**
 * Exception class that signals a username is not valid
 */
public class InvalidPasswordException extends Exception {
    /**
     * default Exception constructor
     * @param message exception message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
