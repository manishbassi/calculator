package com.interview.calculator.parser;

import com.interview.calculator.exception.CalculatorException;
import org.junit.jupiter.api.Test;

import static com.interview.calculator.constants.Constants.DIVIDE_BY_ZERO;
import static com.interview.calculator.constants.Constants.INVALID_EXPRESSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionParserTest {

    @Test
    void simpleAdd() throws CalculatorException {
        String expression = "add(1,2)";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(3, expressionParser.getResult());
    }

    @Test
    void simpleSubtract() throws CalculatorException {
        String expression = "sub(5,2)";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(3, expressionParser.getResult());
    }

    @Test
    void simpleMultiply() throws CalculatorException {
        String expression = "mult(4,6)";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(24, expressionParser.getResult());
    }

    @Test
    void simpleDivide() throws CalculatorException {
        String expression = "div(14,7)";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(2, expressionParser.getResult());
    }

    @Test
    void nestedOperations1() throws CalculatorException {
        String expression = "add(1, mult(2,3))";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(7, expressionParser.getResult());
    }

    @Test
    void nestedOperations2() throws CalculatorException {
        String expression = "mult(add(2,2), div(9,3))";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(12, expressionParser.getResult());
    }

    @Test
    void nestedOperations3() throws CalculatorException {
        String expression = "mult(add(4,3), sub(mult(5,2),add(4,3)))";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(21, expressionParser.getResult());
    }

    @Test
    void nestedOperations4() throws CalculatorException {
        String expression = "div(mult(add(sub(5,4),mult(2,3)),div(9,3)), add(10, 11))";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(1, expressionParser.getResult());
    }

    @Test
    void simpleLet() throws CalculatorException {
        String expression = "let(a, 5, add(a,a))";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(10, expressionParser.getResult());
    }

    @Test
    void nestedLet1() throws CalculatorException {
        String expression = "let(a, 5, let(b, mult(a, 10), add(b,a))";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(55, expressionParser.getResult());
    }

    @Test
    void nestedLet2() throws CalculatorException {
        String expression = "let(a, let(b, 10, add(b,b)), let(b, 20, add(a, b))";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(40, expressionParser.getResult());
    }

    @Test
    void nestedLet3() throws CalculatorException {
        String expression = "let(a, let(b, 10, mult(add(b,b), sub(-b, 2)), mult(a, a))";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertEquals(25600, expressionParser.getResult());
    }

    @Test
    void testNullExpression() {
        String expression = null;
        ExpressionParser expressionParser = new ExpressionParser(expression);
        Exception exception = assertThrows(CalculatorException.class, () -> expressionParser.getResult());
        assertEquals(INVALID_EXPRESSION, exception.getMessage());
    }

    @Test
    public void outOfRangeExpression() {
        String num = String.valueOf(Integer.MAX_VALUE + 1);
        String expression = "add(" + num + ", 1)";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        Exception exception = assertThrows(CalculatorException.class, () -> expressionParser.getResult());
        assertEquals(INVALID_EXPRESSION, exception.getMessage());
    }

    /**
     * Multiplying 5 with Interger.MAX_VALUE should throw an Arithmetic
     * exception
     */
    @Test
    public void numOutOfBoundMultiplication() throws CalculatorException {
        String outOfRangeNum = String.valueOf(Integer.MAX_VALUE);
        String expression = "mult(" + outOfRangeNum + ", 2)";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertThrows(CalculatorException.class, () -> expressionParser.getResult());
    }

    /**
     * Test divide by zero exception
     */
    @Test
    public void divideByZero() {
        String expression = "div(6, 0)";
        ExpressionParser expressionParser = new ExpressionParser(expression);
        Exception exception = assertThrows(ArithmeticException.class, () -> expressionParser.getResult());
        assertEquals(DIVIDE_BY_ZERO, exception.getMessage());
    }
}