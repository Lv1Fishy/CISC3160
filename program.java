
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
                if(line.endsWith(";")){
                    line = line.substring(0, line.length() - 1); // Remove the semicolon at the end of the line

                    if(line.contains("=")){
                        String[] token = line.split("="); // Split the line into two parts: variable name and expression
                        
                        if(token.length != 2){
                            // If the line does not have a variable name/expression or contains multiple =, break
                            variables.clear();
                            break;
                        }
                        if (Identifier(token[0].trim())) {
                            if(!variables.containsKey(token[0].trim())){
                                int value = Exp(token[1].trim());
                                variables.put(token[0].trim(), value); // Store the variable and its value
                            }
                            else{
                                int value = Exp(token[1].trim());
                                variables.replace(token[0].trim(), value);
                            }                            
                        }
                    }
                }
                else{
                    // If the line does not end with a semicolon, break
                    variables.clear();
                    break;
                }
            }
        }
        catch(IllegalArgumentException e){
            variables.clear();
        }

        if(!variables.isEmpty()){
            System.out.println("Output:");
            for (String key : variables.keySet()) {
                System.out.println(key + " = " + variables.get(key));
            }
        }
        else{
            System.out.println("Output:");
            System.out.println("error");
        }
        System.out.println();
        variables.clear();
    }




    public static int Exp(String exp){
        int depth = 0;
        for(int i = 0; i < exp.length(); i++){
            //checks if the expression is not contained within the parenthesis
            if(exp.charAt(i) == '('){
                depth++; //if there is an opening parenthesis, add one to depth to avoid pattern matching + or -
            }
            else if (exp.charAt(i) == ')'){
                depth--; //once a closing parenthesis is found, allow for pattern matching
            }
            else if (depth == 0 && (String.valueOf(exp.charAt(i)).matches("[+-]"))){
                //checks if the previous character does not contain -, +, *, or if it does not exist
                //e.g. x = y+z will get tokenized, x = -y will not get tokenized and will proceed to Term(Fact("-y"))
                if(i > 0 && exp.charAt(i) == '+') {
                    if(!(String.valueOf(exp.charAt(i-1)).matches("[*+-]"))) {
                        String left = exp.substring(0,i); 
                        String right = exp.substring(i+1); 
                        return Exp(left) + Exp(right);
                    }
                }
                else if(i > 0 && exp.charAt(i) == '-'){
                    if(!(String.valueOf(exp.charAt(i-1)).matches("[*+-]"))) {
                        String left = exp.substring(0,i); 
                        String right = exp.substring(i+1); 
                        return Exp(left) - Exp(right);
                    }
                }
            } 
        }
        //if no more binary + or - is found, pass it to Term()
        return Term(exp);
    }
    




    public static int Term(String term){
        int depth = 0;
        for(int i = 0; i < term.length(); i++){
            if(term.charAt(i) == '('){
                depth++;
            }
            else if(term.charAt(i) == ')'){
                depth--;
            }
            else if (depth == 0 && (term.charAt(i) == '*')) {
                if(i == 0){
                    throw new IllegalArgumentException();
                }
                String left = term.substring(0, i);
                String right = term.substring(i+1);
                return Term(left) * Term(right);         
            }
        }
        return Fact(term);
    }




    public static int Fact(String fact){
        if(fact.startsWith("+")){
            return Fact(fact.substring(1));
        }
        else if(fact.startsWith("-")) {
            return -Fact(fact.substring(1));
        }
        else if(fact.startsWith("(") && fact.endsWith(")")){
            int depth = 0;
            for (int i = 0; i < fact.length(); i++) {
                if(fact.charAt(i) == '('){
                    depth++;
                }
                else if (fact.charAt(i) == ')') {
                    depth--;
                }
            }
            if(depth != 0) {
                throw new IllegalArgumentException();
            }
            return Exp(fact.substring(1, fact.length()-1));
        }
        else if(Identifier(fact)) {
            if (variables.containsKey(fact)) {
                return variables.get(fact);
            }
            else{
                throw new IllegalArgumentException();
            }
        }
        else if (!Literal(fact)){
            throw new IllegalArgumentException();   
        }
        return Integer.parseInt(fact); 
    }
    



    /*
     * Identifier() - Identifies the variable name
     * This method checks the first character of the variable name
     * The variable name should not start with a digit
     */

    public static boolean Identifier(String token){
        token = token.trim(); // Remove leading and trailing whitespace
        if(!Letter(token.charAt(0))){ // Checks if the first character is not a letter
            return false; // Return false
        }
        for(int i = 1; i < token.length(); i++){
            if(!(Letter(token.charAt(i)) || Digit(token.charAt(i)))) { // Check if the character is a letter or digit
                return false;
            }
        }
        return true;
    }




    public static boolean Letter(char letter){
        return Pattern.matches("^[a-zA-Z_]$", String.valueOf(letter));
    }




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




    public static boolean NonZeroDigit(char nonZero){
        return Pattern.matches("^[1-9]$", String.valueOf(nonZero));
    }




    public static boolean Digit(char digit) {
        return Pattern.matches("^[0-9]$", String.valueOf(digit));
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