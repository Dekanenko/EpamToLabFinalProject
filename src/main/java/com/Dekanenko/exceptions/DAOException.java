package com.Dekanenko.exceptions;

public class DAOException extends Exception{
    private String message;

    public DAOException() {
    }

    public DAOException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
