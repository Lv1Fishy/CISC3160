/*********************************************************************************************************************************************
 * The following defines a simple language, in which a program consists of assignments and each variable is assumed to be of the integer type. 
 * For the sake of simplicity, only operators that give integer values are included. 
 * Write an interpreter for the language in a language of your choice. 
 * Your interpreter should be able to do the following for a given program: 
 * (1) detect syntax errors; 
 * (2) report uninitialized variables; and 
 * (3) perform the assignments if there is no error and print out the values of all the variables after all the assignments are done.
 *********************************************************************************************************************************************/

import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args) {

        try {
            File file = new File(args[0]);
            Scanner scanner = new Scanner(file);

            

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + args[0]);
        }
        System.out.println("Terminating Program.");
    }

    public static void Assignment(){
        
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