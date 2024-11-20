package src;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {

        StringBuffer input1 = new StringBuffer();
        StringBuffer input2 = new StringBuffer();
        StringBuffer output = null;
        
       // read and make a stringbuffer from input files
        try {
            int character;

            FileReader read = new FileReader("text_file\\input1.txt");
            while ( (character = read.read())!=-1) { 
                input1.append((char) character);

                //checks if char is not numbers
                if(character < 48 || character > 58){
                    System.out.println("invalid input1");
                    System.exit(0);
                }
            }
            read.close();

            read  = new FileReader("text_file\\input2.txt");
            while ( (character = read.read())!=-1) { 
                input2.append((char) character);

                if(character < 48 || character > 58){
                    System.out.println("invalid input2");
                    System.exit(0);
                }
            }
            read.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("something's wrong");
        }

        //handle terminal input
        Scanner equations = new Scanner(System.in);
        System.out.println("type in the equations symbol you want to do [+ *]");
        switch (equations.nextLine().charAt(0)) {
            case '+' -> output = Plus(input1, input2);
            case '*' -> output = Multiply(input1, input2);
            default -> System.out.println("nothing found");
        }    
        equations.close();
    
        //write the output
        try {
            FileWriter fwOutput = new FileWriter("text_file\\output.txt");
            
            fwOutput.write(output.charAt(0));
            
            for(int i=1; i<output.length();i++){
                fwOutput.append(output.charAt(i));
            }

            fwOutput.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("something's wrong");
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

        //handle addition
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

        //add leftover over to the left
        if(over!=0){
            output.insert(0, over); 
        }
        return output;
    }


    private static StringBuffer Multiply(StringBuffer input1, StringBuffer input2){
        StringBuffer output = new StringBuffer();
   
        /*
        idea, idk what this method of multiplication is called
                    11
                    11 
                    __ x
                    11
                   11  
                   ___ +
                   121 
        */

        // determines which is the bigger number (to speed up the process, choose the smallerStr)
        byte select = 0;
        StringBuffer[] bORs = {input1, input2};
        if(input1.length()<input2.length()){
            select = 1;
        }

        int biggerStr = bORs[(0 + select) % 2].length();
        int smallerStr = bORs[(1 + select) % 2].length();

        boolean continueCount = true;

        byte over = 0;
        byte dgtResult = 0;

        while (continueCount) {
            
            int icount = bORs[(1 + select) % 2].length() - smallerStr;
            icount = output.length()-icount;

            // for every char in the biggerStr, multiply with all the char in smallerStr
            for(int i=biggerStr-1; i>=0; i--){

                dgtResult = (byte) (bORs[(0 + select) % 2].charAt(i)-48);
                dgtResult = (byte) (dgtResult * (bORs[(1 + select) % 2].charAt(smallerStr-1)-48));
                dgtResult += over;
                over = (byte) (dgtResult/10);

                dgtResult %= 10;

                //handle multiplication
                if(smallerStr == bORs[(1 + select) % 2].length()){
                    output.insert(0, dgtResult);

                    //add over to the left and reset to 0
                    if(over!=0 && i==0){
                        output.insert(0, over);
                        over=0;
                    }
                }
                else if(icount==0){
                    output.insert(0, dgtResult+over);
            
                }

                //handle addition during multiplication
                else{
                    byte plusResult = (byte) ((output.charAt(icount-1)-48)+dgtResult);
                    over = (byte) (over + (plusResult/10));
                    plusResult %= 10;
                    output.replace(icount-1, icount, ""+plusResult);
                    icount--;

                    if(over!=0 && i==0){
                        output.insert(0, over);
                        over = 0;
                    }
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