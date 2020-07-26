package com.interview.calculator.controller;

import com.interview.calculator.exception.CalculatorException;
import org.junit.jupiter.api.Test;

import static com.interview.calculator.constants.Constants.INVALID_EXPRESSION;
import static com.interview.calculator.constants.Constants.INVALID_LOG_LEVEL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MainControllerTest {

    @Test
    public void invalidLogLevel() {
        String args[] = {"add(-1,-3)", "test"};
        Exception exception = assertThrows(CalculatorException.class, () -> MainController.main(args));
        assertEquals(INVALID_LOG_LEVEL, exception.getMessage());
    }

    @Test
    public void emptyExpression() {
        String args[] = {"", "warn"};
        Exception exception = assertThrows(CalculatorException.class, () -> MainController.main(args));
        assertEquals(INVALID_EXPRESSION, exception.getMessage());
    }

    @Test
    public void invalidExpression() {
        String args[] = {"invalid", "debug"};
        Exception exception = assertThrows(CalculatorException.class, () -> MainController.main(args));
        assertEquals(INVALID_EXPRESSION, exception.getMessage());
    }
}