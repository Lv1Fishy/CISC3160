/*********************************************************************************************************************************************
 * The following defines a simple language, in which a program consists of assignments and each variable is assumed to be of the integer type. 
 * For the sake of simplicity, only operators that give integer values are included. 
 * Write an interpreter for the language in a language of your choice. 
 * Your interpreter should be able to do the following for a given program: 
 * (1) detect syntax errors; 
 * (2) report uninitialized variables; and 
 * (3) perform the assignments if there is no error and print out the values of all the variables after all the assignments are done.
 *********************************************************************************************************************************************/

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

import java.util.*;

public class main {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) { 

            while (true) { 
                List<String> parseLines = new ArrayList<>(); // Arraylist to store lines for parsing

                System.out.println("Enter Program Statements (Empty to Exit Program): ");
                String str = input.nextLine(); // Read the first line of input

                if(str.isEmpty()) // If User enters an empty line, break the loop and terminate the program
                    break; 

                parseLines.add(str); // Add the line to the list for parsing

                while (true) {  // To read multiple assignment statements
                    str = input.nextLine();

                    if(str.isEmpty()) 
                        break; // Check if the line is empty or null

                    parseLines.add(str); // Add the line to the list for parsing
                }
                
                Program(parseLines); // Begin parsing the assignment statements

            }

            input.close();
        } catch (Exception e) {
            System.out.println("Error");
        } 
        System.out.println("Terminating Program.");
    }

    public static void Program(List<String> parseLines) {
        for (String line : parseLines) {
            if(line.endsWith(";")){
                line = line.substring(0, line.length() - 1); // Remove the semicolon at the end of the line
                Assignment(line); // Process the assignment statement
            }
            else{
                System.out.println("error"); // If the line does not end with a semicolon, print error
                return; // Exit the method
            }
        }
    }

    public static void Assignment(String line){
        if (!line.contains("=")) {
            System.out.println("error"); // If the line does not contain an assignment operator, print error
            return; // Exit the method
        }
        HashMap<String, Integer> variables = new HashMap<>(); // HashMap to store variable names and their values

        
        
    }

    public static void Exp(){

    }

    public static void Term(){

    }

    public static void Fact(){

    }
    
    public static void Identifier(){
            
            
           
    }

    public static void Letter(){

    }

    public static void Literal(){

    }

    public static void NonZeroDigit(){

    }

    public static void Digit(){

    }

}