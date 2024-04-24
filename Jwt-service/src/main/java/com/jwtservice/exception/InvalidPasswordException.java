package com.jwtservice.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Invalid Password");
    }

    public InvalidPasswordException(String message) {
            super(message);
    }

    public InvalidPasswordException(String message,Throwable cause) {
        super(message,cause);
    }
}
