# calculator
### Summary
Calculator program in Java that evaluates expressions in a very simple integer expression language. The program takes an input on the command line, computes the result, and prints it to the console. 

For example :
java calculator.Main "add(2, 2)"
4

### Assumptions :
•	Numbers: integers between Integer.MIN_VALUE and Integer.MAX_VALUE.

•   Final result can be outside the Integer range as divison can result in float numbers.	

•	Variables: strings of characters, where each character is one of a-z, A-Z

•	Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments.  In other words, each argument may be any of the expressions on this list.

•	A “let” operator for assigning values to variables:
	let(<variable name>, <value expression>, <expression where variable is used>)
	
As with arithmetic functions, the value expression and the expression where the variable is used may be an arbitrary expression from this list. 

### Build this project
#### Continuous Integration
It has been integrated with codeship for Continuos integration. Build will be triggered automatically once new commit
/merge is detected to master branch.

https://app.codeship.com/projects/969f4df0-b11d-0138-f1be-0a5e7ff5c824

#### Manual Commands
We can also build this project manually using the following commands :

```
git clone https://github.com/manishbassi/calculator.git
cd calculator
mvn clean install
```

### Run this project
Once build, we can use jar with suffic (jar-with-dependencies), We can use following command to send out expressions and logging level to the jar

```
java -jar target/calculator-1.0-jar-with-dependencies.jar "add(1,2)" "info"
```