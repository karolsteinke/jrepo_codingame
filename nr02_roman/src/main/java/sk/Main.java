package sk;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        try (Scanner scanner = new Scanner(System.in)) {
            
            while (true) {
                
                System.out.println("Insert roman time in format HOUR:MINUTE (or leave empty for exit):");
                String str = scanner.nextLine();
                
                if (str.isEmpty()) {
                    break;
                }
                else {
                    String[] array = str.split(":");
                    String hh = array[0];
                    String mm = array[1];
                    System.out.println("Time in arabic: " + translate(hh) + ":" + translate(mm));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int translate(String roman) {
        //translates numbers up to 89 to arabic format
        
        char[] chars = roman.toCharArray();
        int result = 0;
        
        for (int i=0; i < chars.length; i++) {
            if (chars[i] == 'I') {
                //case "IV"
                if (i+1 < chars.length && chars[i+1] == 'V') {
                    result += 4;
                    i++;
                }
                //case "IX"
                else if (i+1 < chars.length && chars[i+1] == 'X') {
                    result += 9;
                    i++;
                }
                //case "I"
                else {
                    result += 1;
                }
            }
            else if (chars[i] == 'X') {
                //case "XL"
                if (i+1 < chars.length && chars[i+1] == 'L') {
                    result += 40;
                    i++;
                }
                //case "X"
                else {
                    result += 10;
                }
            }
            //case "V"
            else if (chars[i] == 'V') {
                result += 5;
            }
            //case "L"
            else if (chars[i] == 'L') {
                result += 50;
            }
        }

        return result;
    }
}
