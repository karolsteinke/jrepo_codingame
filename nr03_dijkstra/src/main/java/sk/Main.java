package sk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//TO DO: dense graph scenario (|V|^2 edges) doesn't really need any alg! So - remove some edges from the problem

public class Main {
    public static void main(String[] args) {

        ArrayList<Node> nodes = new ArrayList<>();
        HashMap<Integer,Integer> dist = new HashMap<>(); //working dict
        HashMap<Integer,Integer> result = new HashMap<>(); //result dict
        
        //1. User inpput
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Insert node's coordinates: X,Y (leave empty for exit):");
            while (true) {
                String str = scanner.nextLine();
                if (str.isEmpty()) {
                    break;
                }
                else {
                    String[] split = str.split(",");
                    Node node = new Node(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
                    nodes.add(node);
                    //assume source is first added node
                    dist.put(nodes.size()-1, (nodes.size()>1) ? Integer.MAX_VALUE : 0);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        //2. Dijkstra alg
        for (int i=0; i < nodes.size(); i++) {
            
            //find idxA (idx to node with shortest dist atm)
            int minA = Integer.MAX_VALUE;
            int idxA = -1;
            for (int key : dist.keySet()) {
                if (dist.get(key) < minA) {
                    minA = dist.get(key);
                    idxA = key;
                }
            }
            Node a = nodes.get(idxA);

            //save dist to A as shortest path, remove it from working dict
            result.put(idxA, minA);
            dist.remove(idxA);

            //update distances from A to other nodes
            if (!dist.isEmpty()) {
                for (int key : dist.keySet()) {
                    Node b = nodes.get(key);
                    
                    //l = pythagorean for [A;B] plus shortest path to A
                    double l = Math.hypot(a.getX()-b.getX(), a.getY()-b.getY());
                    l += minA;
                    if (l < dist.get(key)) {
                        dist.put(key, (int) l);
                    }
                }
            }
        }

        //3. Print results
        for (int key : result.keySet()) {
            System.out.println("Shortest path for " + key + " is: " + result.get(key));
        }

    }
}

class Node {
    int x;
    int y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {return this.x;}
    public int getY() {return this.y;}

    @Override
    public String toString() {
        return ("[node " + this.x + ", " + this.y + "]");
    }
}