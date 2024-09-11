package sk;

import java.util.*;

//idea: use try with resources for Scanner.in
//idea: use try-catch once for whole switch?
// "SYNTAX ERROR" if a [ appears to have no ] to jump to, or vice versa. Note that this error must be raised before the execution of the program, no matter its position in the Brainfuck code.
// "POINTER OUT OF BOUNDS" if the pointer position goes below 0 or above S - 1.
// "INCORRECT VALUE" if after an operation the value of a cell becomes negative or higher than 255.

class Solution {

    public static void main(String args[]) {
        
        // ***** Input *****

        Scanner in = new Scanner(System.in);
        
        // input: L S N definition (1 line)
        int L = in.nextInt(); //# of instructions lines
        int S = in.nextInt(); //'s' array size, every cell = one byte value
        int N = in.nextInt(); //# of integer input lines
        if (in.hasNextLine()) {
            in.nextLine();
        }

        ArrayList<Character> instructions = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();
        RestrictedInt[] memory = new RestrictedInt[S];
        String answer = "";
        for (int i=0; i<memory.length; i++) {
            memory[i] = new RestrictedInt(0, 0, 255);
        }
        
        // input: instructions, add to 'instructions' ArrayList (L lines)
        for (int i = 0; i < L; i++) {
            String input = in.nextLine();
            //ignore everything but instructions
            String replace = input.replaceAll("[^+\\-,.<>\\[\\]]", "");
            for (char c : replace.toCharArray()) {
                instructions.add(c);
            }
        }        

        // input: integers, add to 'numbers' ArrayList (N lines)
        for (int i = 0; i < N; i++) {
            String input = in.nextLine();
            String[] split = input.split(" ");
            for (String n : split) {
                //ignore everything but integers
                try { 
                    int val = Integer.parseInt(n);
                    numbers.add(val);
                } catch (NumberFormatException e) { 
                    System.err.println("The string is not an integer and will be ignored, string: " + n); 
                } 
            }
        }

        in.close();

        // ***** Syntax check *****

        //inner class
        class SyntaxChecker {
            
            int i = 0;
            int bracketsDepth = 0;

            public boolean isSyntaxValid() {
                while (i < instructions.size()) {
                    char ins = instructions.get(i);
                    
                    if (ins == '[') {
                        i++;
                        bracketsDepth++;
                        boolean isSyntaxValid = isSyntaxValid();
                        if (!isSyntaxValid) return isSyntaxValid;
                    }
                    else if (ins == ']') {
                        if (bracketsDepth > 0) {
                            i++;
                            bracketsDepth--;
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                        i++;
                    }
                }
                return (bracketsDepth == 0);
            }

        }

        SyntaxChecker checker = new SyntaxChecker();
        if (!checker.isSyntaxValid()) {
            System.out.println("SYNTAX ERROR");
            return;
        }

        // ***** Program interpretation *****
        
        Iterator<Integer> numIter = numbers.iterator();
        ListIterator<Character> insIter = instructions.listIterator();
        RestrictedInt j = new RestrictedInt(0, 0, S-1);

        while (insIter.hasNext()) {
            
            char ins = insIter.next();
            //System.err.println(ins + " j=" + j.getValue() + ", memory[j]=" + memory[j.getValue()].getValue());
            
            switch(ins) {
                case '>':
                    //increment the pointer position
                    try {
                        j.increment();
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("POINTER OUT OF BOUNDS");
                        return;
                    }
                    break;
                case '<':
                    //decrement the pointer position
                    try {
                        j.decrement();
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("POINTER OUT OF BOUNDS");
                        return;
                    }
                    break;
                case '+':
                    //increment the value of the cell the pointer is pointing to
                    try {
                        memory[j.getValue()].increment();
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("INCORRECT VALUE");
                        return;
                    }
                    break;
                case '-':
                    //decrement the value of the cell the pointer is pointing to
                    try {
                        memory[j.getValue()].decrement();
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("INCORRECT VALUE");
                        return;
                    }
                    break;
                case '.':
                    //output the value of the pointed cell, interpreting it as an ASCII value
                    answer += (char) memory[j.getValue()].getValue();
                    break;
                case ',':
                    //accept a positive one byte integer as input and store it in the pointed cell
                    if (numIter.hasNext()) {
                        try {
                            int a = numIter.next();
                            memory[j.getValue()] = new RestrictedInt(a, 0, 255);
                        }
                        catch (IllegalArgumentException e) {
                            System.out.println("INCORRECT VALUE");
                            return;
                        }
                    }
                    break;
                case '[':
                    //jump to the instruction after the corresponding ] if the pointed cell's value is 0
                    if (memory[j.getValue()].getValue() == 0) {
                        int corresponding = 1;
                        while (corresponding != 0) {
                            ins = insIter.next();
                            if (ins == ']') corresponding--;
                            else if (ins == '[') corresponding++;
                        }
                    }
                    break;
                case ']':
                    //go back to the instruction after the corresponding [ if the pointed cell's value is different from 0
                    if (memory[j.getValue()].getValue() != 0) {
                        int corresponding = -1;
                        insIter.previous();
                        while (corresponding != 0) {
                            ins = insIter.previous();
                            if (ins == ']') corresponding--;
                            else if (ins == '[') corresponding++;
                        }
                    }
                    break;
            }
        }

        // ***** Printing answer *****

        // Write an answer using System.out.println()
        // To debug: System.err.println("Debug messages...");

        System.out.println(answer);
    }
}

class RestrictedInt {
    private int val;
    private int min;
    private int max;

    public RestrictedInt(int val, int min, int max) {
        this.val = val;
        this.min = min;
        this.max = max;
    }
    public int getValue() {
        return this.val;
    }
    public void setValue(int val) {
        if (val > this.max || val < this.min) {
            throw new IllegalArgumentException();
        }
        this.val = val;
    }
    public void increment() {
        setValue(this.val + 1);
    }
    public void decrement() {
        setValue(this.val - 1);
    }   
}