package com.projectTest.demo.exceptions;

public class ServiceException extends RuntimeException {

    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
