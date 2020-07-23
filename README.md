# command-line-calculator
Calculator program in Java that evaluates expressions in a very simple integer expression language. The program takes an input on the command line, computes the result, and prints it to the console. For example :
% java calculator.Main "add(2, 2)"
4

An expression is one of the of the following:
•	Numbers: integers between Integer.MIN_VALUE and Integer.MAX_VALUE
•	Variables: strings of characters, where each character is one of a-z, A-Z
•	Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments.  In other words, each argument may be any of the expressions on this list.
•	A “let” operator for assigning values to variables:
	let(<variable name>, <value expression>, <expression where variable is used>)
As with arithmetic functions, the value expression and the expression where the variable is used may be an arbitrary expression from this list. 
