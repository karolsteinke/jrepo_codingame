package sk;

import java.util.*;
import java.io.*;
import java.math.*;
import java.nio.file.Paths;

public class Player {

    public static void main(String[] args) {
        //try (Scanner in = new Scanner(System.in)) {
        try (Scanner in = new Scanner(Paths.get("zombie_input.txt"))) {
            
            // game loop
            while (true) {
                int x = in.nextInt(); //character pos
                int y = in.nextInt(); //character pos
                int humanCount = in.nextInt();
                for (int i = 0; i < humanCount; i++) {
                    int humanId = in.nextInt();
                    int humanX = in.nextInt();
                    int humanY = in.nextInt();
                }
                int zombieCount = in.nextInt();
                for (int i = 0; i < zombieCount; i++) {
                    int zombieId = in.nextInt();
                    int zombieX = in.nextInt();
                    int zombieY = in.nextInt();
                    int zombieXNext = in.nextInt();
                    int zombieYNext = in.nextInt();
                }

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                System.out.println("0 0"); // Your destination coordinates

                //DEBUG, check for end of test input
                if (x == -1) break;
            }
            //end of game loop
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

class Human {
    private int id;
    private int x;
    private int y;

    public Human(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int[] pos() {return new int[] {this.x, this.y};}
    public int id() {return this.id;}
}

class Zombie extends Human {
    private int xNext;
    private int yNext;

    public Zombie(int id, int x, int y, int xNext, int yNext) {
        super(id, x, y);
        this.xNext = xNext;
        this.yNext = yNext;
    }

    public int[] posNext() {return new int[] {this.xNext, this.yNext};}
}

