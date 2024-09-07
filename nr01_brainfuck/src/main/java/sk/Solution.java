package sk;

import java.util.*;
import java.io.*;

//idea: use try with resources for Scanner.in
//idea: use try-catch once for whole switch?
//problem: exception wont stop excecution
//idea: isSyntaxValid and distanceToClosingBrackets do the same, so reduce!

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
        char[] memory = new char[S]; 
        String answer = "";
        
        // input: instructions lines (L lines) - add to instructions ArrayList
        for (int i = 0; i < L; i++) {
            String input = in.nextLine();
            String replace = input.replaceAll("[^+\\-,.<>\\[\\]]", "");
            for (char c : replace.toCharArray()) {
                instructions.add(c);
            }
        }        

        // input: integer input lines (N lines) - add to numbers ArrayList
        for (int i = 0; i < N; i++) {
            String input = in.nextLine();
            String[] split = input.split(" ");
            for (String n : split) {
                try { 
                    int val = Integer.parseInt(n);
                    numbers.add(val);
                } catch (NumberFormatException e) { 
                    System.err.println("The string is not an integer, string: " + n); 
                } 
            }
        }

        in.close();


        // ***** Syntax check *****

        //inner class
        class SyntaxChecker {
            
            int idx = 0;

            public boolean isSyntaxValid() {
                
                while (true) {
                    
                    char instruction = instructions.get(this.idx);
                    
                    //instruction is ']'
                    if (instruction == ']') {
                        return false;
                    }
                    //instruction is '['
                    else if (instruction == '[') {
                        int distanceToClosingBracket = distanceToClosingBracket(this.idx);
                        if (distanceToClosingBracket == -1) return false;
                        else this.idx += distanceToClosingBracket;
                    }
                    //instruction is something else
                    else {
                        this.idx++;
                    }
                    
                    //end of instructions arraylist
                    if (this.idx >= instructions.size()) break;
                }
                return true;
            }

            private int distanceToClosingBracket(int from) {
                
                int distance = 1;
                int newIdx = from + distance;
                
                while(true) {
                    
                    char instruction = instructions.get(newIdx);

                    //instruction is ']'
                    if (instruction == ']') {
                        return distance;
                    }
                    //instruction is '['
                    else if (instruction == '[') {
                        int distanceToClosingBracket = distanceToClosingBracket(newIdx);
                        if (distanceToClosingBracket == -1) return -1;
                        else distance += distanceToClosingBracket;
                    }
                    //instruction is something else
                    else {
                        distance++;
                    }
                    
                    //end of instructions arraylist
                    if (newIdx >= instructions.size()) return -1;
                }
            }
        }

        SyntaxChecker checker = new SyntaxChecker();
        if (!checker.isSyntaxValid()) {
            System.out.println("SYNTAX ERROR");
        }

        // ***** Program interpretation *****
        
        Iterator<Integer> numIter = numbers.iterator();
        ListIterator<Character> insIter = instructions.listIterator();
        RestrictedInt memoIdx = new RestrictedInt(0, 0, S);

        while (insIter.hasNext()) {
            
            char ins = insIter.next();
            
            switch(ins) {
                case '>':
                    //increment the pointer position
                    try {
                        memoIdx.setValue(memoIdx.getValue() + 1);
                    }
                    catch (PointerOutOfBounds e) {
                        System.out.println("POINTER OUT OF BOUNDS");
                    }
                    break;
                case '<':
                    //decrement the pointer position
                    try {
                        memoIdx.setValue(memoIdx.getValue() - 1);
                    }
                    catch (PointerOutOfBounds e) {
                        System.out.println("POINTER OUT OF BOUNDS");
                    }
                    break;
                case '+':
                    //increment the value of the cell the pointer is pointing to
                    memory[memoIdx.getValue()]++;
                    break;
                case '-':
                    //decrement the value of the cell the pointer is pointing to
                    memory[memoIdx.getValue()]--;
                    break;
                case '.':
                    //output the value of the pointed cell, interpreting it as an ASCII value
                    answer += memory[memoIdx.getValue()];
                    break;
                case ',':
                    //accept a positive one byte integer as input and store it in the pointed cell
                    if (numIter.hasNext()) {
                        int a = numIter.next();
                        memory[memoIdx.getValue()] = (char) a;
                    }
                    break;
                case '[':
                    //jump to the instruction after the corresponding ] if the pointed cell's value is 0
                    if (memory[memoIdx.getValue()] == 0) {
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
                    if (memory[memoIdx.getValue()] != 0) {
                        int corresponding = -1;
                        while (corresponding != 0) {
                            ins = insIter.previous();
                            if (ins == ']') corresponding--;
                            else if (ins == '[') corresponding++;
                        }
                    }
                    break;
                default:
                    //different instruction
            }
        }


        // ***** Printing answer *****

        // Write an answer using System.out.println()
        // To debug: System.err.println("Debug messages...");

        /*
        char x = 'a';
        answer += x;
        x++;
        answer += x;
        */

        System.out.println(answer);
    }
}

class PointerOutOfBounds extends RuntimeException {
    public PointerOutOfBounds() {}
    public PointerOutOfBounds(String message) {
        super(message);
    }
}

class RestrictedInt {
    private int value;
    private int min;
    private int max;

    public RestrictedInt(int value, int min, int max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }
    public void setValue(int value) {
        if (value > this.max || value < this.min) {
            throw new PointerOutOfBounds();
        }
        this.value = value;
    }
    public int getValue() {
        return this.value;
    }
}