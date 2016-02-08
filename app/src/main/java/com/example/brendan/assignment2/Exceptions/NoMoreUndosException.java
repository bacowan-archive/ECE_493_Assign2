package com.example.brendan.assignment2.Exceptions;

/**
 * Created by Brendan on 2/7/2016.
 */
public class NoMoreUndosException extends RuntimeException {
    public NoMoreUndosException(String message) {
        super(message);
    }
    public NoMoreUndosException(Exception e) { super(e); }
}
