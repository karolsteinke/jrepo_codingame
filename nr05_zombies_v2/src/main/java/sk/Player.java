package sk;

import java.util.*;
import java.nio.file.Paths;

public class Player {

    public static void main(String[] args) {
        
        //try (Scanner in = new Scanner(System.in)) {
        try (Scanner in = new Scanner(Paths.get("zombie_input.txt"))) {
            
            //GAME LOOP
            while (true) {
                int xNext = 0;
                int yNext = 0;
                Set<Zombie> zombies = new HashSet<>();
                Set<Human> humans = new HashSet<>();
                
                int x = in.nextInt(); //character pos
                int y = in.nextInt(); //character pos
                int humanCount = in.nextInt();
                for (int i = 0; i < humanCount; i++) {
                    int humanId = in.nextInt();
                    int humanX = in.nextInt();
                    int humanY = in.nextInt();
                    humans.add(new Human(humanId, humanX, humanY));
                }
                int zombieCount = in.nextInt();
                for (int i = 0; i < zombieCount; i++) {
                    int zombieId = in.nextInt();
                    int zombieX = in.nextInt();
                    int zombieY = in.nextInt();
                    int zombieXNext = in.nextInt();
                    int zombieYNext = in.nextInt();
                    zombies.add(new Zombie(zombieId, zombieX, zombieY, zombieXNext, zombieYNext));
                }

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                

                //OUTPUT RESULT
                System.out.println(xNext + " " + yNext); // Your destination coordinates

                //DEBUG
                if (x == -1) break;
            }
            //END OF GAME LOOP
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

    public int x() {return this.x;}
    public int y() {return this.y;}
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

    public int xNext() {return this.xNext;}
    public int yNext() {return this.yNext;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Zombie)) return false;
        Zombie otherZombie = (Zombie) other;
        return otherZombie.id() == this.id() && otherZombie.x() == this.x() && otherZombie.y() == this.y();
    }

    @Override
    public int hashCode() {
        return this.id();
    }
}