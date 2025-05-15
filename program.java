//CISC 3160
//Jiahui Yu
//Project Assignment

import java.io.*;
import java.util.*;
import java.util.regex.*;

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

            if(line.isEmpty()) { // Once an empty line is encountered, proceed to parse the sub-program lines
                Assignment(assignmentLines); // Send the sub-program lines to the Assignment method for parsing
                assignmentLines.clear(); // Clear the list to reset for the next sub-program
            }
            else{
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
        System.out.println("Input:");
        for(String line : assignmentLines) {
            System.out.println(line);
        }
        System.out.println();
        try{
            for (String line : assignmentLines) { // Iterate through each line from the sub-program lines

                if(line.endsWith(";")){ // Check if code ends with semicolon

                    line = line.substring(0, line.length() - 1); // Remove the semicolon at the end of the line
                    if(line.contains("=")){ // Check if there is an assignment statement

                        String[] token = line.split("="); // Split the line into two parts: variable name and expression
                        if(token.length != 2){
                            // If the line does not have a variable name/expression or contains multiple assignment statements, break
                            variables.clear();
                            break; //break out of for loop
                        }
                        if (Identifier(token[0].trim())) { //check for valid variable identifier
                            if(!variables.containsKey(token[0].trim())){ //check if the hashmap does not contains a existing variable
                                int value = Exp(token[1].trim()); 
                                variables.put(token[0].trim(), value); // Store the variable and its value
                            }
                            else{ // If it does contain an existing variable, replace that variable's data
                                int value = Exp(token[1].trim());
                                variables.replace(token[0].trim(), value);
                            }                            
                        }
                    }
                }
                else{
                    // If the line does not end with a semicolon, break the forloop
                    variables.clear();
                    break;
                }
            }
        }
        catch(IllegalArgumentException e){ //handles error exceptions from expressions
            variables.clear();
        }

        //If variables exists in the Hashmap, print out the hashmap's key and its value
        if(!variables.isEmpty()){
            System.out.println("Output:");
            for (String key : variables.keySet()) {
                System.out.println(key + " = " + variables.get(key));
            }
        }
        else{ //if there is no variables in the hashmap, then there is an error. i.e. error detected, no variables have no value, etc.
            System.out.println("Output:");
            System.out.println("error");
        }
        System.out.println();
        variables.clear(); //reset for next assignment statements.
    }



    /******************************************************************************************************
     * Exp() - handles expression statements
     * Exp -> Exp + Term | Exp - Term | Term
     * It handles binary operations such as + and - by recursive calls 
     * Also handles if the expression contains parenthesis so that it can ignore the expression inside it 
     * once all the binary operations is done, move to Term()
     ******************************************************************************************************/
    public static int Exp(String exp){
        int depth = 0;
        for(int i = 0; i < exp.length(); i++){
            //checks if the expression is contained within the parenthesis
            if(exp.charAt(i) == '('){
                depth++; //if there is an opening parenthesis, add one to depth to avoid pattern matching + or -
            }
            else if (exp.charAt(i) == ')'){
                depth--; //once a closing parenthesis is found, allow for pattern matching
            }
            else if (depth == 0 && (String.valueOf(exp.charAt(i)).matches("[+-]"))){
                //checks if the previous character does not contain -, +, *, or if it does not exist
                //e.g. x = y+z will get tokenized, x = -y will not get tokenized and will proceed to Term(Fact("-y"))
                if(i > 0 && exp.charAt(i) == '+') { //check if the operation is + and not the first index to avoid unary +
                    if(!(String.valueOf(exp.charAt(i-1)).matches("[*+-]"))) { //check if previous character does not contain any operations
                        String left = exp.substring(0,i); //left recursion
                        String right = exp.substring(i+1); //right recursion
                        return Exp(left) + Exp(right);
                    }
                }
                else if(i > 0 && exp.charAt(i) == '-'){ //check if the operation is - and not the first index to avoid unary -
                    if(!(String.valueOf(exp.charAt(i-1)).matches("[*+-]"))) { //check if previous character does not contain any operations
                        String left = exp.substring(0,i); //left recursion
                        String right = exp.substring(i+1); //right recursion
                        return Exp(left) - Exp(right);
                    }
                }
            } 
        }
        //if no more binary + or - is found, pass it to Term()
        return Term(exp);
    }
    


    /**********************************************************************************************
     * Term() - handles multiplication operation
     * Term -> Term * Fact  | Fact
     * This method also handles parenthesis to make sure it doesn't matches the multiplication inside it
     * It also checks if there is no consecutive * 
     * Once all of the terms are handled, move to Fact()
     **********************************************************************************************/
    public static int Term(String term){
        int depth = 0;
        for(int i = 0; i < term.length(); i++){
            //checks if the expression is contained within the parenthesis
            if(term.charAt(i) == '('){
                depth++; //if there is an opening parenthesis, add one to depth to avoid pattern matching *
            }
            else if(term.charAt(i) == ')'){
                depth--; //once a closing parenthesis is found, allow for pattern matching
            }
            else if (depth == 0 && (term.charAt(i) == '*')) {
                if(i == 0){ //if beginning char start with *, throw exception since there is no unary *
                    throw new IllegalArgumentException();
                }
                String left = term.substring(0, i); //left recursion
                String right = term.substring(i+1); //right recursion
                return Term(left) * Term(right);         
            }
        }
        // If no more * is found, move to Fact()
        return Fact(term);
    }



    /*
     * Fact() - Handles parenthesis, unary operations - and +, literals, and identifiers
     * Fact -> ( Exp ) | - Fact | + Fact | Literal | Identifier
     * This method first checks for unary operations
     * It Recurses if the first character of the string contains the unary operations 
     * Once there is no more unary operations to check for, start checking for any parenthesis
     * Handle any unbalanced parenthesis, if there is an unbalanced amount, throw an exception.
     * Otherwise, call back to Exp() with the outer parenthesis stripped.
     * If there is no more Unary operations and parenthesis, check if the string is Literal or an Identifier.
     * If the identifier is valid, check if there is an existing identifier in the Hashmap. If there's none, throw an Exception
     * If there is an existing identifier, return that identifier's value
     * Check if the string is a literal. If it's not, throw an exception.
     * Otherwise, return an int value that is converted from string to int
     */
    public static int Fact(String fact){
        if(fact.startsWith("+")){ //check for unary +
            //unary + does nothing. If x = +(-5), x will still be -5
            return Fact(fact.substring(1)); 
        }
        else if(fact.startsWith("-")) { //check for unary -
            //unary - switches the value from positive to negative and vice versa
            return -Fact(fact.substring(1));
        }
        else if(fact.startsWith("(") && fact.endsWith(")")){ //check if the parenthesis begins with ( and ends with )
            int depth = 0;
            //handles if parenthesis is balanced
            for (int i = 0; i < fact.length(); i++) {
                if(fact.charAt(i) == '('){
                    depth++;
                }
                else if (fact.charAt(i) == ')') {
                    depth--;
                }
            }
            if(depth != 0) { //if not balanced, throw an exception
                throw new IllegalArgumentException();
            }
            //otherwise, return the Exp() with the expression inside the parenthesis
            return Exp(fact.substring(1, fact.length()-1));
        }
        else if(Identifier(fact)) { //checks if string is a variable
            if (variables.containsKey(fact)) { //checks if the variable exists in the hashmap
                return variables.get(fact); //return the variable's value
            }
            else{ //if no variable exists in the hashmap, throw an exception
                throw new IllegalArgumentException();
            }
        }
        else if (!Literal(fact)){ //checks if string is a valid Literal
            throw new IllegalArgumentException(); //throw exception if not
        }
        return Integer.parseInt(fact); //otherwise, return the converted string to int's value
    }



    /*******************************************************************
     * Identifier() - Identifies the variable name
     * This method checks the first character of the variable name
     * The variable name should not start with a digit
     *******************************************************************/
    public static boolean Identifier(String token){
        token = token.trim(); // Remove leading and trailing whitespace
        if(!Letter(token.charAt(0))){ // Checks if the first character is not a letter
            return false; // Return false
        }
        //loop through the string
        for(int i = 1; i < token.length(); i++){
            if(!(Letter(token.charAt(i)) || Digit(token.charAt(i)))) { // Check if the character is a letter or digit
                return false;
            }
        }
        return true;
    }



    /*****************************************************************************
     * Letter() - handles pattern matching for variable names
     * Letter -> a|...|z|A|...|Z|_
     * Return true if char is a lowercase, uppercase character, or an underscore
     * Return false if otherwise 
     *****************************************************************************/
    public static boolean Letter(char letter){
        return Pattern.matches("^[a-zA-Z_]$", String.valueOf(letter));
    }



    /******************************************************************************************************************
     * Literal() - handles any zero or non-leading zero integer
     * Literal -> 0 | NonZeroDigit Digit*
     * Returns true if the string is only 0 or non-leading zero integer
     * Returns false if there are leading 0 in the beginning of the string or if the string contains any characters
     ******************************************************************************************************************/
    public static boolean Literal(String literal){
        if(literal.equals("0")) {
            return true; // Valid literal for zero
        } else if (literal.charAt(0) == '0' || literal.length() > 1) { // Leading zeros are not allowed
            return false;
        }
        else if(NonZeroDigit(literal.charAt(0))) { // Check if the first character is a non-zero digit
            for(int i = 1; i < literal.length(); i++){
                if(!Digit(literal.charAt(i))) { // Check if the character is a digit
                    return false;
                }
            }
        }
        else{
            // If the literal starts with a character, print error
            return false;
        }
        return true; //String that starts with a non-zero and contains only leading digits
    }



    /*****************************************************************
     * NonZeroDigit() - handles any non-zero digit
     * NonZeroDigit -> 1|...|9
     * Returns true if character is from 1 to 9, false if otherwise
     *****************************************************************/
    public static boolean NonZeroDigit(char nonZero){
        return Pattern.matches("^[1-9]$", String.valueOf(nonZero));
    }



    /******************************************************************
     * Digit() - handles any digits
     * Digit -> 0|1|...|9
     * Returns true if character is from 0 to 9, false if otherwise
     ******************************************************************/
    public static boolean Digit(char digit) {
        return Pattern.matches("^[0-9]$", String.valueOf(digit));
    }
}
