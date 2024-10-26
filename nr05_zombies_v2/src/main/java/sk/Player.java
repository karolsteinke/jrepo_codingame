package sk;

import java.util.*;
import java.nio.file.Paths;

//TO DO: NullPointerException

class Player {

    public static void main(String[] args) {
        
        //divide map (16000x9000) into 8x5=40 sectors with centres based on hex-grid
        int sectorH = 1732;
        int sectorW = 2000;

        //try (Scanner in = new Scanner(System.in)) {
        try (Scanner in = new Scanner(Paths.get("zombie_input.txt"))) {
            
            //GAME LOOP
            while (true) {
                int[][] zombiesInSector = new int[8][5];
                HashMap<Integer, Human> humans = new HashMap<>();
                HashMap<Integer, HashSet<Zombie>> humanToZombies = new HashMap<>();
                HashMap<Integer, Boolean> isHumanDead = new HashMap<>();
                HashMap<Integer, Double> humanToAshDist = new HashMap<>();

                int x = in.nextInt(); //Ash pos
                int y = in.nextInt(); //Ash pos
                int humanCount = in.nextInt();
                for (int i = 0; i < humanCount; i++) {
                    int humanId = in.nextInt();
                    int humanX = in.nextInt();
                    int humanY = in.nextInt();
                    double dist = Math.hypot(humanX-x, humanY-y);
                    humans.put(humanId, new Human(humanX, humanY));
                    humanToZombies.put(humanId, new HashSet<>());
                    isHumanDead.put(humanId, false);
                    humanToAshDist.put(humanId, dist);
                }
                int zombieCount = in.nextInt();
                for (int i = 0; i < zombieCount; i++) {
                    int zombieId = in.nextInt();
                    int zombieX = in.nextInt();
                    int zombieY = in.nextInt();
                    int zombieXNext = in.nextInt();
                    int zombieYNext = in.nextInt();

                    //match zombie with closest human
                    double min = Double.MAX_VALUE;
                    int targetId = 0;
                    for (int id : humans.keySet()) {
                        Human h = humans.get(id);
                        double dist = Math.hypot(h.x()-zombieX, h.y()-zombieY);
                        if (dist < min) {
                            min = dist;
                            targetId = id;
                        }
                    }
                    humanToZombies.get(targetId).add(new Zombie(zombieX, zombieY, zombieXNext, zombieYNext));
                    
                    //verify if zombie kills before Ash could reach; Ash moves 2,5x times faster
                    if (min < humanToAshDist.get(targetId)/2.5) {
                        isHumanDead.put(targetId, true);
                    }
                }

                //assign zombies to a sector
                for (int id : humanToZombies.keySet()) {
                    if (!isHumanDead.get(id)) {
                        for (Zombie z : humanToZombies.get(id)) {
                            int sectorRow = Math.max (0, Math.min((z.yNext() - 170)/sectorH, 4)); //{0,1,2,3,4}
                            int sectorCol; //{0,1,2,3,4,5,6,7}
                            if (sectorRow % 2 == 0) sectorCol = z.xNext()/sectorW; //even rows have offset to the right, odd rows don't
                            else sectorCol = Math.max(0, Math.min((z.xNext() - 1000)/sectorW, 6)); //dont assign to 7th index here (outside map)
                            zombiesInSector[sectorCol][sectorRow]++;
                        }
                    }
                }
                
                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                double bestScore = 0.0;
                int xNext = 0;
                int yNext = 0;

                //choose sector to target
                for (int i=0; i<8; i++) {
                    for (int j=0; j<5; j++) {

                        int zombieNumber = zombiesInSector[i][j];
                        
                        if (zombieNumber != 0) {
                            //current sector center x,y
                            int centerY = j*sectorH + 170 + sectorH/2;
                            int centerX = 0;
                            if (j%2 != 1) centerX = i*sectorW + sectorW/2;
                            else centerX = i*sectorW + 1000 + sectorW/2;
                            
                            //score based on zombies, distance to Ash, humans alive
                            double distToAsh = Math.hypot(x - centerX, y - centerY);
                            double distToHumanPow = Double.MAX_VALUE;
                            for (int id : humans.keySet()) {
                                if (!isHumanDead.get(id)) {
                                    Human h = humans.get(id);
                                    double d = Math.pow(h.x() - centerX, 2) + Math.pow(h.y() - centerY, 2);
                                    if (d < distToHumanPow) {
                                        distToHumanPow = d;
                                    }
                                }
                            }
                            double score = zombieNumber / distToAsh / distToHumanPow;
                            
                            //save score to var if it's the best
                            if (score > bestScore) {
                                bestScore = score;
                                xNext = centerX;
                                yNext = centerY;
                            }
                        }
                    }
                }

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
    private int x;
    private int y;

    public Human(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {return this.x;}
    public int y() {return this.y;}
}

class Zombie extends Human {
    private int xNext;
    private int yNext;

    public Zombie(int x, int y, int xNext, int yNext) {
        super(x, y);
        this.xNext = xNext;
        this.yNext = yNext;
    }

    public int xNext() {return this.xNext;}
    public int yNext() {return this.yNext;}
}