package src;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {

        StringBuffer input1 = new StringBuffer();
        StringBuffer input2 = new StringBuffer();
        
       // read and make a stringbuffer from input files
        try {
            int character;

            FileReader read1 = new FileReader("text_file\\input1.txt");
            FileReader read2 = new FileReader("text_file\\input2.txt");

            while ( (character = read1.read())!=-1) { 
                input1.append((char) character);
            }
            while ( (character = read2.read())!=-1) { 
                input2.append((char) character);
            }

            read1.close();
            read2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("something's wrong");
        }

        Scanner equations = new Scanner(System.in);

        StringBuffer output = null;

        System.out.println("type in the equations symbol you want to do [+]");
        switch (equations.nextLine().charAt(0)) {
            case '+':
                output = Plus(input1, input2);
                break;
            default:
                System.out.println("nothing found");
        }
        equations.close();
    
        try {
            FileWriter overwriteOutput = new FileWriter("text_file\\output.txt");
            FileWriter appendOutput = new FileWriter("text_file\\output.txt", true);
            
            overwriteOutput.write(output.charAt(0));
            
            for(int i=1; i<output.length();i++){
                appendOutput.write(output.charAt(i));
            }

            overwriteOutput.close();
            appendOutput.close();
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

            
        /*  idea, minus the last char by one till 0; once 0 check the left if 0;
            if left is not 0 turn last char to 9 use plus();
            if left is 0 check the left of that char again until reaching charAt(0);
            once all letters is 0 then return output
        */



        return output;
    }

    


}