package com.example.brendan.assignment2.Exceptions;

/**
 * Created by Brendan on 1/15/2016.
 */
public class InvalidFilterType extends RuntimeException {
    public InvalidFilterType(String message) {
        super(message);
    }
    public InvalidFilterType(Exception e) { super(e); }
}
