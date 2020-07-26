package com.interview.calculator.parser;

import ch.qos.logback.classic.Logger;
import com.interview.calculator.constants.ArithmeticFunctions;
import com.interview.calculator.exception.CalculatorException;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static com.interview.calculator.constants.Constants.*;

/**
 * Expression Parser class to parse the entire expression and returns the result of the expression.
 * <p>
 * Approach : We're maintaining three different stacks for - variables, operands and functions and use Map to store the values of variables.
 * We will parse the string character by character as follows :
 * - if character is digit (a-z, A-Z), append value to string (function)
 * - if character is number (0-9), append value to number (number)
 * - if character is open bracket '(', we push the function value to "functions" stack and reset function string
 * - if character is comma (,) , we compute let expressions or operands
 * - we compute variables as part of let expessions if top function of stack is let, and store them in map
 * - we compute operands and push to operands stack
 * - if character is closing bracket ')', we will calculate the recent expression using the values from the stacks and variableMap,
 * and pushes the computed result back to operands stack
 * <p>
 * Once the entire string is passed, operands stack will have the result value.
 * <p>
 * if expression is invalid, operands stack will be empty and will throw appropriate exception accordingly.
 */
public class ExpressionParser {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ExpressionParser.class);
    Stack<Float> operands;
    Stack<ArithmeticFunctions> functions;
    Stack<String> variables;
    Map<String, Float> variableMap;
    String expression;

    public ExpressionParser(String expression) {
        this.operands = new Stack<>();
        this.functions = new Stack<>();
        this.variables = new Stack<>();
        this.variableMap = new HashMap<>();
        this.expression = expression;
    }

    /**
     * Computes the result of entire expression.
     *
     * @return
     * @throws CalculatorException
     */
    public float getResult() throws CalculatorException {
        if (null == expression || expression.length() == 0) {
            LOGGER.info("Invalid expression : {}" + expression);
            throw new CalculatorException(INVALID_EXPRESSION);
        }
        Integer number = null;
        StringBuilder function = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                number = number == null ? 0 : number;
                number = 10 * number + c - '0';
            } else if (Character.isLetter(c)) {
                function.append(c);
            } else if (c == '(') {
                ArithmeticFunctions arithmeticFunction = ArithmeticFunctions.getEnum(function.toString());
                functions.push(arithmeticFunction);
                function.setLength(0);
            } else if (c == ',') {
                computeVariablesAndOperands(number, function);
                number = null;
            } else if (c == ')') {
                if (number != null) {
                    validateNumber(number);
                    operands.push(Float.valueOf(number));
                }
                computeRecentExpression(function);
                number = null;
            }
        }
        if (operands.isEmpty())
            throw new CalculatorException(INVALID_EXPRESSION);
        return operands.pop();
    }

    /**
     * Returns the function value performing respective arithmetic operation
     *
     * @param function
     * @param operand1
     * @param operand2
     * @return
     * @throws CalculatorException
     */
    private float getFunctionValue(ArithmeticFunctions function, float operand1, float operand2) throws CalculatorException {
        if (null == function)
            return 0;
        switch (function) {
            case ADD:
                return operand1 + operand2;
            case SUB:
                return operand2 - operand1;
            case MULT:
                return operand1 * operand2;
            case DIV:
                if (operand1 == 0) {
                    throw new ArithmeticException(DIVIDE_BY_ZERO);
                }
                return operand2 / operand1;
            default:
                throw new CalculatorException(INVALID_FUNCTION);
        }
    }

    /**
     * Computes variables and operands as part of let expression evaluation.
     * <p>
     * We'll evaluate let expression if top value of functions stack is "let" and store them in variableMap otherwise
     * we will evaluate the operands and push to operands stack.
     *
     * @param number
     * @param function
     */
    private void computeVariablesAndOperands(Integer number, StringBuilder function) throws CalculatorException {
        if (functions.peek() == ArithmeticFunctions.LET && !variables.isEmpty()) {
            calculateLetExpression(number, function);
        } else if (functions.peek() == ArithmeticFunctions.LET) {
            variables.push(function.toString());
            function.setLength(0);
        } else {
            if (variableMap.containsKey(function.toString())) {
                operands.push(variableMap.get(function.toString()));
                function.setLength(0);
            } else if (number != null) {
                validateNumber(number);
                operands.push(Float.valueOf(number));
            }
        }
    }

    /**
     * This method calculate let expressions
     *
     * @param number
     * @param variable
     */
    private void calculateLetExpression(Integer number, StringBuilder variable) {
        if (number == null) {
            if (!operands.isEmpty()) {
                variableMap.put(variables.pop(), operands.pop());
                functions.pop();
            } else {
                variables.push(variable.toString());
                variable.setLength(0);
            }
        } else {
            variableMap.put(variables.pop(), Float.valueOf(number));
            functions.pop();
        }
    }

    /**
     * This method computes the value of recent expression and pushes the result back to stack of operands
     *
     * @param variable
     * @throws CalculatorException
     */
    private void computeRecentExpression(StringBuilder variable) throws CalculatorException {
        Float functionValue = null;
        if (variableMap.containsKey(variable.toString())) {
            functionValue = getFunctionValue(functions.pop(), operands.pop(), variableMap.get(variable.toString()));
            variable.setLength(0);
        } else if (operands.size() > 1) {
            functionValue = getFunctionValue(functions.pop(), operands.pop(), operands.pop());
        }
        if (functionValue != null)
            operands.push(functionValue);
    }

    /**
     * Validates the input numbers.
     * Number should be within Integer range.
     *
     * @param number
     * @throws CalculatorException
     */
    private void validateNumber(float number) throws CalculatorException {
        int num = (int) number;
        if(num >= Integer.MAX_VALUE || num <= Integer.MIN_VALUE)
            throw new CalculatorException(OUT_OF_RANGE);
    }
}
