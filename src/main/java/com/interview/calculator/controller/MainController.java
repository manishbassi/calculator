package com.interview.calculator.controller;

import ch.qos.logback.classic.Logger;
import com.interview.calculator.constants.LogLevel;
import com.interview.calculator.exception.CalculatorException;
import com.interview.calculator.parser.ExpressionParser;
import com.interview.calculator.util.LoggingUtil;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.interview.calculator.constants.Constants.*;

/**
 * Main Controller class to start the execution.
 * Main class to accept user parameters and evaluate the value of the expression
 */
public class MainController {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MainController.class);

    public static void main(String[] args) throws CalculatorException {
        //Validate arguments
        validateArguments(args);
        String expression = args[0];
        ExpressionParser expressionParser = new ExpressionParser(expression);
        LOGGER.info("Result of the expression : {} is : {}", expression, expressionParser.getResult());
    }

    /**
     * Method to validate user arguments
     *
     * @param args
     * @throws CalculatorException
     */
    private static void validateArguments(String[] args) throws CalculatorException {
        if (null == args || args.length == 0) {
            LOGGER.warn("Proper usage: java -jar calculator-1.0-SNAPSHOT-jar-with-dependencies.jar <EXPRESSION> <OPTIONAL-LOG-LEVEL>");
            throw new CalculatorException(INVALID_ARGUMENTS);
        }
        if (args.length > 1) {
            String logLevel = args[1];
            if (LoggingUtil.isSupported(logLevel)) {
                LoggingUtil.setApplicationLogLevel(logLevel);
            } else {
                LOGGER.warn("Supported log levels are : {}", Arrays.toString(LogLevel.values()));
                throw new CalculatorException(INVALID_LOG_LEVEL);
            }
        }
    }
}
