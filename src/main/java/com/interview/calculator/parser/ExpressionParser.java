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
 * Expression Parser class to parse the entire expression and returns the result of the expression
 *
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

    public float getResult() throws CalculatorException {
        if (null == expression || expression.length() == 0) {
            LOGGER.info("Invalid expression : {}" + expression);
            throw new CalculatorException(INVALID_EXPRESSION);
        }
        Float number = null;
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
                if (number != null)
                    operands.push(number);
                computeRecentExpression(function);
                number = null;
            }
        }
        if (operands.isEmpty())
            throw new CalculatorException(INVALID_EXPRESSION);
        return operands.pop();
    }

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

    private void computeVariablesAndOperands(Float number, StringBuilder function) {
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
                operands.push(number);
            }
        }
    }

    private void calculateLetExpression(Float number, StringBuilder variable) {
        if (number == null) {
            if (!operands.isEmpty()) {
                variableMap.put(variables.pop(), operands.pop());
                functions.pop();
            } else {
                variables.push(variable.toString());
                variable.setLength(0);
            }
        } else {
            variableMap.put(variables.pop(), number);
            functions.pop();
        }
    }

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
}
