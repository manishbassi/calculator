package com.interview.calculator.constants;

import com.interview.calculator.exception.CalculatorException;

import static com.interview.calculator.constants.Constants.INVALID_FUNCTION;

/**
 * Enum to maintain set of eligible arithmetic functions that are allowed.
 */
public enum ArithmeticFunctions {
    ADD("add"), SUB("sub"), MULT("mult"), DIV("div"), LET("let");
    private String value;

    ArithmeticFunctions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ArithmeticFunctions getEnum(String value) throws CalculatorException {
        for (ArithmeticFunctions function : values())
            if (function.getValue().equalsIgnoreCase(value))
                return function;
        throw new CalculatorException(INVALID_FUNCTION);
    }
}
