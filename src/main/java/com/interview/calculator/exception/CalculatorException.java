package com.interview.calculator.exception;

/**
 * Custom exception class to have appropriate messages
 *
 */
public class CalculatorException extends Exception {

    public CalculatorException() {
        super();
    }

    public CalculatorException(String message) {
        super(message);
    }
}
