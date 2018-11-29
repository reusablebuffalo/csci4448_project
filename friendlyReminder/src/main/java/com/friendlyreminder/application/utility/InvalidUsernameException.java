package com.friendlyreminder.application.utility;

/**
 * Exception class that signals that a username is not valid
 */
public class InvalidUsernameException extends Exception {
    /**
     * default Exception constructor
     * @param message exception message
     */
    public InvalidUsernameException(String message) {
        super(message);
    }
}