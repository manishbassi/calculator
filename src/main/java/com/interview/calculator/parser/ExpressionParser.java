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
 * Approach : We're maintaining three different stacks for - variables, operands and functions and using a Map to store the values of variables.
 * We will parse the string character by character as follows :
 * - if character is digit (a-z, A-Z), append value to string (function).
 * - if character is number (0-9), append value to number (number).
 * - if character is open bracket '(', we push the function value to "functions" stack and reset function string.
 * - if character is comma (,) , we compute let expressions or operands.
 * - we compute variables as part of let expressions if top function of stack is let, and store them in map.
 * - we compute operands and push to operands stack.
 * - if character is closing bracket ')', we will calculate the recent expression using the values from the stacks and variableMap,
 * and pushes the computed result back to operands stack.
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
        // Considering Wrapper class because 0 can also be one of the numbers.
        Integer number = null;
        StringBuilder function = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                // Appending every digit to the number to have the final number.
                number = number == null ? 0 : number;
                number = 10 * number + c - '0';
            } else if (Character.isLetter(c)) {
                // Appending every letter to function to have function names like "add", "sub" etc.
                function.append(c);
            } else if (c == OPEN_BRACKET) {
                // We will validate the function name and push it to functions stack.
                ArithmeticFunctions arithmeticFunction = ArithmeticFunctions.getEnum(function.toString());
                functions.push(arithmeticFunction);
                // Resetting the function string so that it can have next value.
                function.setLength(0);
            } else if (c == COMMA) {
                // We'll evaluate the variables and operands as per the stack values
                computeVariablesAndOperands(number, function);
                // Resetting the number value to calculate the next value
                number = null;
            } else if (c == CLOSE_BRACKET) {
                if (number != null) {
                    validateNumber(number);
                    // Pushing float value to operands as division can lead to decimal places
                    operands.push(Float.valueOf(number));
                }
                // Compute the recent expression by getting top values of every stack
                computeRecentExpression(function);
                // Resetting the number value to calculate the next value
                number = null;
            }
        }
        if (operands.isEmpty())
            throw new CalculatorException(INVALID_EXPRESSION);
        // Return the last calculated value in operands stack.
        return operands.pop();
    }

    /**
     * Returns the calculated value after performing the respective arithmetic operation.
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
     * If let is at top of the function stack, we'll evaluate let expression and store them in variableMap otherwise
     * we will evaluate the operands and push to operands stack.
     *
     * @param number
     * @param function
     */
    private void computeVariablesAndOperands(Integer number, StringBuilder function) throws CalculatorException {
        // If recent function is "let", and variables stack already has a value, we'll evaluate that expression
        if (functions.peek() == ArithmeticFunctions.LET && !variables.isEmpty()) {
            calculateLetExpression(number, function);
        } else if (functions.peek() == ArithmeticFunctions.LET) {
            // if variables stack is empty, we'll push the variable to that stack and reset the function (as it will contain the variable name)
            variables.push(function.toString());
            function.setLength(0);
        } else {
            // if the operand is variable, we will fetch it's valaue from the map and push it to operands stack, then resetting the variable name
            if (variableMap.containsKey(function.toString())) {
                operands.push(variableMap.get(function.toString()));
                function.setLength(0);
            } else if (number != null) {
                // if operand is number instead of variable, we'll validate and then push it operands stack.
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
            // if operands stack is not empty, then top most value of operands stack will be value of that variable of let expression.
            if (!operands.isEmpty()) {
                variableMap.put(variables.pop(), operands.pop());
                // let expression is calculated, hence, function will be removed from the stack.
                functions.pop();
            } else {
                // if operands stack is empty and number is null, we'll push variable to variables stack, so that we'll evaluate it later
                variables.push(variable.toString());
                variable.setLength(0);
            }
        } else {
            // if number value is valid, we will add the top most value of variable to map.
            variableMap.put(variables.pop(), Float.valueOf(number));
            // let expression is calculated, hence, function will be removed from the stack.
            functions.pop();
        }
    }

    /**
     * This method computes the value of recent expression and pushes the result back to stack of operands.
     * <p>
     * Recent expression takes the top most values of stacks
     * - function will be picked from functions stack
     * - operands will be picked from operands stack.
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
        if (num >= Integer.MAX_VALUE || num <= Integer.MIN_VALUE)
            throw new CalculatorException(OUT_OF_RANGE);
    }
}
