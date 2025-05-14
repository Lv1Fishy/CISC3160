
import java.util.*;
import java.util.regex.*;
import java.io.*;



public class program {

    //Global variable
    static HashMap<String, Integer> variables = new HashMap<>(); // HashMap to store variable names and their values
    
    /************************************************************************************
     * main() - Beginning of the program
     * This method reads an input file from command line argument
     * It reads each line from the file and stores it in an ArrayList
     * After reading all lines from the file, it calls Program() method for the next step
     ************************************************************************************/
    public static void main(String[] args) {
        try (Scanner input = new Scanner(new File(args[0]))) {  //get input file from command line argument
            List<String> parseLines = new ArrayList<>(); // Arraylist to store lines for parsing

            while (input.hasNextLine()) { 
                String str = input.nextLine(); // Read each line from the input file
                parseLines.add(str);

                //System.out.println(str);

            }

            Program(parseLines); // Begin parsing the assignment statements
            input.close();

        } catch (Exception e) {
            System.out.println("Input File Error");
        } 
        System.out.println("Terminating Program.");
    }



    /****************************************************************************************************
     * Program() - Creates sub assignment lines for parsing
     * This method creates another list that stores the sub-program lines
     * It keeps adding each line until an empty line is encountered
     * Once an empty line is encountered, it calls the Assignment() method to parse the sub-program lines
     * After Assignment() method is done, it clears the list to reset for the next sub-program
     *****************************************************************************************************/
    public static void Program(List<String> parseLines) {
        if(parseLines.isEmpty()) {
            System.out.println("Empty Program Error"); // If the input is empty, print error
            return; // Exit the method
        }
        List<String> assignmentLines = new ArrayList<>(); // List to parse lines until empty line
        for (String line : parseLines) { // Iterate through each line from the input file

            //System.out.println(line);

            if(line.isEmpty()) { // Once an empty line is encountered, proceed to parse the sub-program lines
                
                //System.out.println("Foo");

                Assignment(assignmentLines); // Send the sub-program lines to the Assignment method for parsing
                assignmentLines.clear(); // Clear the list to reset for the next sub-program
            }
            else{

                //System.out.println("hello");

                assignmentLines.add(line); 
            }

        }
    }



    /*
     * Assignment() - Parses each assignment line
     * This method handles errors for semicolons ';' and assignment operator '='
     * It then calls Identifier() method to assign the variable name to a global object HashMap to keep track of variables
     * It also calls Exp() method to map the value to the variable name
     */
    public static void Assignment(List<String> assignmentLines) {
        if(assignmentLines.isEmpty()) {
            return; // Exit the method
        }
        for (String line : assignmentLines) { // Iterate through each line from the sub-program lines
            if(line.endsWith(";")){
                line = line.substring(0, line.length() - 1); // Remove the semicolon at the end of the line

                if(line.contains("=")){
                    String[] token = line.split("="); // Split the line into two parts: variable name and expression
                    
                    if(token.length != 2){
                        System.out.println("Missing variable/Expression Error"); // If the line does not have a variable name/expression, print error
                        return; // Exit the method
                    }
                    //REQUIRE RECODE************************************************************** 
                    
                    if(Identifier(token[0].trim())){
                        System.out.println(token[0].trim());
                        if (variables.containsKey(token[0].trim())) {
                            //variables.replace(token[0].trim(), Exp(token[1]));
                        }
                        else {
                            //variables.put(token[0], token[1]); // Add the variable name and its value to the HashMap
                        }
                    }
                    //****************************************************************************
                }
            }
            else{
                System.out.println("Missing Semicolon Error"); // If the line does not end with a semicolon, print error
                return; // Exit the method
            }
        }
    }




    public static void Exp(String exp){
        if (exp.contains("+")) { // Check if the expression contains '+' or '-'
            String[] token = exp.split("+", 2); // Split the expression into two parts at the first '+'
            Exp(token[0]);
            Exp(token[1]);
        }
        else if (exp.contains("-")) { // Check if the expression contains '-' or '+'
            String[] token = exp.split("-", 2); // Split the expression into two parts at the first '-'
            Exp(token[0]);
            Exp(token[1]);
        }
        else{
            Term(exp); // If no '+' or '-', call Term() method
        }
    }




    public static void Term(String term){
        if(term.contains("*")) { // Check if the term contains '*'
            String[] token = term.split("\\*", 2); // Split the term into two parts at the first '*'
            Term(token[0]);
            Term(token[1]);
        }
        else{
            Fact(term); // If no '*' or '/', call Fact() method
        }
    }




    public static void Fact(String fact){
        if(fact.contains("(")){
            int startIndex = fact.indexOf("(")+1; // Find the index of the first '('
            String subFact = fact.substring(startIndex); // Get the substring after the '('
            if(subFact.contains(")")) {
                int endIndex = subFact.indexOf(")"); // Find the index of the first ')'
                String innerFact = subFact.substring(0, endIndex); // Get the substring between '(' and ')'
                Exp(innerFact); // Call Fact() method with the inner fact
            }
            else{
                System.out.println("Missing \")\" Error"); // If no ')' is found, print error
                return;
            }
        }
        else if(fact.contains("-")) { // Check if the fact contains '-'
            String[] token = fact.split("-", 2); // Split the fact into two parts at the first '-'
            Fact(token[0]); // Call Fact() method with the first part
            Fact(token[1]); // Call Fact() method with the second part
        }
        else if(fact.contains("+")) { // Check if the fact contains '+'
            String[] token = fact.split("\\+", 2); // Split the fact into two parts at the first '+'
            Fact(token[0]); // Call Fact() method with the first part
            Fact(token[1]); // Call Fact() method with the second part
        }
        else{
            Literal(fact); // If no '+' or '-', call Literal() method
            if(Identifier(fact)){
                if(variables.containsKey(fact)){
                    System.out.println(fact + " = " + variables.get(fact)); // Print the variable name and its value
                }
                else{
                    System.out.println("error"); // If the variable is not found, print error
                    return;
                }
            }
            else{
                System.out.println("error"); // If the fact is not a valid identifier, print error
                return;
            }
        }
    }
    



    /*
     * Identifier() - Identifies the variable name
     * This method checks the first character of the variable name
     * The variable name should not start with a digit
     */

    public static boolean Identifier(String token){
        //System.out.println("Identifier called?: " + token);
        System.out.println(token.length());
        token.trim(); // Remove leading and trailing whitespace
        System.out.println(token.length());
        if(!Letter(token.charAt(0))){
            System.out.println("Beginning Variable Character Error"); // If the first character is not a letter, print error
            return false; // Exit the method
        }
        for(int i = 1; i < token.length(); i++){

            //System.out.println(!(Letter(token.charAt(i)) || Digit(token.charAt(i))));

            if(!(Letter(token.charAt(i)) || Digit(token.charAt(i)))) { // Check if the character is a letter or digit
                System.out.println("Unknown Character Error");
                return false;
            }
        }
        return true;
    }




    public static boolean Letter(char letter){

        //System.out.println(Pattern.matches("^[a-zA-Z_]$", String.valueOf(letter)) + " " + letter);

        if(Pattern.matches("^[a-zA-Z_]$", String.valueOf(letter))) { // Check if the character is a letter or underscore
            return true; 
        } 
        return false; 
    }




    public static void Literal(String literal){
        if(literal.equals("0")) {
            return; // Valid literal for zero
        } else if (literal.charAt(0) == '0') {
            System.out.println("Leading Zero Error"); // Leading zeros are not allowed
            return;
        }
        else if(NonZeroDigit(literal.charAt(0))) { // Check if the first character is a non-zero digit
            for(int i = 1; i < literal.length(); i++){
                if(!Digit(literal.charAt(i))) { // Check if the character is a digit
                    System.out.println(" Not a Digit Error"); // If not, print error
                    return;
                }
            }

        }
        else{
            System.out.println("Invalid Literal Error"); // If the literal is not valid, print error
            return;
        }

    }




    public static boolean NonZeroDigit(char nonZero){
        if(Pattern.matches("^[1-9]$", String.valueOf(nonZero))) { // Check if the character is a non-zero digit (1-9)
            return true;
        }
        return false;
    }




    public static boolean Digit(char digit) {

        //System.out.println(Pattern.matches("^[0-9]$", String.valueOf(digit)) + " " + digit);

        if(Pattern.matches("^[0-9]$", String.valueOf(digit))) { // Check if the character is a digit (0-9)
            return true; 
        }
        return false;
    }
    
}




/*********************************************************************************************************************************************
 *          Assignment:
 * The following defines a simple language, in which a program consists of assignments and each variable is assumed to be of the integer type. 
 * For the sake of simplicity, only operators that give integer values are included. 
 * Write an interpreter for the language in a language of your choice. 
 * Your interpreter should be able to do the following for a given program: 
 * (1) detect syntax errors; 
 * (2) report uninitialized variables; and 
 * (3) perform the assignments if there is no error and print out the values of all the variables after all the assignments are done.
 **********************************************************************************************************************************************/

 /*
    Program:
       Assignment*

    Assignment:
        Identifier = Exp;

    Exp: 
        Exp + Term | Exp - Term | Term

    Term:
        Term * Fact  | Fact

    Fact:
        ( Exp ) | - Fact | + Fact | Literal | Identifier

    Identifier:
            Letter [Letter | Digit]*

    Letter:
        a|...|z|A|...|Z|_

    Literal:
        0 | NonZeroDigit Digit*
            
    NonZeroDigit:
        1|...|9

    Digit:
        0|1|...|9
  */

  /*
    Sample Input and Output:

    Input 1
    x = 001;

    Output 1
    error

    Input 2
    x_2 = 0;

    Output 2
    x_2 = 0

    Input 3
    x = 0
    y = x;
    z = ---(x+y);

    Output 3
    error

    Input 4
    x = 1;
    y = 2;
    z = ---(x+y)*(x+-y);

    Output 4
    x = 1
    y = 2
    z = 3
   */