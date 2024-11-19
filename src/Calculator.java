package src;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {

        StringBuffer input1 = new StringBuffer();
        StringBuffer input2 = new StringBuffer();
        
       // read and make a stringbuffer from input files
        try {
            int character;

            FileReader read = new FileReader("text_file\\input1.txt");
            while ( (character = read.read())!=-1) { 
                input1.append((char) character);
            }
            read.close();

            read  = new FileReader("text_file\\input2.txt");
            while ( (character = read.read())!=-1) { 
                input2.append((char) character);
            }
            read.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("something's wrong");
        }

        Scanner equations = new Scanner(System.in);

         StringBuffer output = null;

        System.out.println("type in the equations symbol you want to do [+ *]");

        switch (equations.nextLine().charAt(0)) {
            case '+' -> output = Plus(input1, input2);
            case '*' -> output = Multiply(input1, input2);
            default -> System.out.println("nothing found");
        }
        
        equations.close();
    
        try {
            FileWriter fwOutput = new FileWriter("text_file\\output.txt");
            
            fwOutput.write(output.charAt(0));
            
            for(int i=1; i<output.length();i++){
                fwOutput.append(output.charAt(i));
            }

            fwOutput.close();
        } catch (Exception ex) {
        }
    }




    private static StringBuffer Plus(StringBuffer input1, StringBuffer input2){
        StringBuffer output = new StringBuffer();

        // determines which is the bigger number
        byte select = 0;
        StringBuffer[] bORs = {input1, input2};
        if(input1.length()<input2.length()){
            select = 1;
        }

        int biggerStr = bORs[(0 + select) % 2].length();
        int smallerStr = bORs[(1 + select) % 2].length();
        
        /*
         * take the last char in both stringbuffer turn it into byte and add them together 
         * (if there is no more char from a smaller number then add itself into output)
         * over carries the value number if it exceeds 9
         */

        byte over = 0;
        for(; biggerStr > 0; --smallerStr) {
            byte result =  (byte) (bORs[(0 + select) % 2].charAt(biggerStr - 1) - 48 + over);
            if (smallerStr > 0) {
                byte add = (byte) (bORs[(1 + select) % 2].charAt(smallerStr - 1) - 48);
                result += add;
            }

            over = (byte) (result / 10);
            result %= 10;
            output.insert(0, result);
            --biggerStr;
        }

        if(over!=0){
            output.insert(0, over); 
        }
        return output;
    }




    private static StringBuffer Multiply(StringBuffer input1, StringBuffer input2){
        StringBuffer output = new StringBuffer();

            
        /*  
        take the last char of a smaller string, multiply with all of the digits in the bigger string
        put in output
        then, do the same thing again, put in output index-1 while, if there is a digit in the specified 
        output index then add them together

        idea, idk what this method of multiplication is called
                    11
                    11 
                    __ x
                    11
                   11  
                   ___ +
                   121 
        */


        // determines which is the bigger number
        byte select = 0;
        StringBuffer[] bORs = {input1, input2};
        if(input1.length()<input2.length()){
            select = 1;
        }

        int biggerStr = bORs[(0 + select) % 2].length();
        int smallerStr = bORs[(1 + select) % 2].length();

        boolean continueCount = true;

        byte overMult = 0; //carry num after multiplication
        byte overPlus = 0; //carry num after addition
        byte dgtResult = 0;

        while (continueCount) {
            
            int icount = bORs[(1 + select) % 2].length() - smallerStr;
            icount = output.length()-icount;

            for(int i=biggerStr-1; i>=0; i--){

                dgtResult = (byte) (bORs[(0 + select) % 2].charAt(i)-48);
                dgtResult = (byte) (dgtResult * (bORs[(1 + select) % 2].charAt(smallerStr-1)-48));
                dgtResult += overMult;
                overMult = (byte) (dgtResult/10);

                dgtResult %= 10;

                if(smallerStr == bORs[(1 + select) % 2].length()){
                    output.insert(0, dgtResult);
                    if(overMult!=0){
                        output.insert(0, overMult);
                    }
                }
                else if(icount==0){
                    output.insert(0, dgtResult+overPlus);
                }
                else{
                    byte plusResult = (byte) ((output.charAt(icount-1)-48)+dgtResult);
                    overPlus = (byte) (plusResult/10);
                    plusResult %= 10;
                    output.replace(icount-1, icount, ""+plusResult);
                    icount--;
                }
            }

            smallerStr -= 1;

            if(smallerStr-1 == -1){
                continueCount = false;
                break;
            }
        }


        return output;
    }

    


}