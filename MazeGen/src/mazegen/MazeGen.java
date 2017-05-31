/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazegen;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author Andy
 */
public class MazeGen {

    //create and initialize new maze
    public Cell[][][][] Alien;
    int size;
    int N;
    int count = 0;
    //00 bits are +-1 movement in that dimension
    //01 bits are -1 movement in that dimension
    //10 bits are +1 movement in that dimension
    final int XL = 1;
    final int XR = 2;
    final int YL = 2 << 1;
    final int YR = 2 << 2;
    final int ZU = 2 << 3;
    final int ZD = 2 << 4;
    final int TF = 2 << 5;
    final int TB = 2 << 6;
    //positions with 0 in coordinate location move only positive
    //positions with n-1 in coordinate location move only negatively
    //each cell can move in at most 8 positions
    //initially all cells are not connected, covered by walls

    public MazeGen(int tsize) {
        N = tsize;
        size = (int) Math.pow(tsize, 4);
        Alien = new Cell[tsize][tsize][tsize][tsize];
        for (int time = 0; time < tsize; time++) {
            for (int Z = 0; Z < tsize; Z++) {
                for (int Y = 0; Y < tsize; Y++) {
                    for (int X = 0; X < tsize; X++) {
                        //all cells initially not connected
                        Alien[time][Z][Y][X] = new Cell(null, 0, 0xff, new int[]{time, Z, Y, X});
                        Alien[time][Z][Y][X].root = Alien[time][Z][Y][X];
                    }
                }
            }
        }
    }

    //Search if cells have same root, all new cells are its own root
    //when traversing to root from cell, make each intermediate cell the child to that root
    //Path compression: Whenever we conduct a find on an element, we make all
    //children on the find path point directly to the root (thereby making the path
    //to the root shorter for all the elements on the path).
    public Cell Find(Cell a) {
        //if it already is root return itself
        if (a.root == a) {
            return a;
        } //otherwise find the main root
        else {
            a.root = (Find(a.root));
            return a.root;
        }
    }

    //if new root encounters cell already in dataset than union new cell with dataset
    //if both cells have same rank(height) choose either cell to be new root increment rank of root
    //if one is higher choose that one
    public void Union(Cell a, Cell b) {
        Cell rootA, rootB;
        rootA = Find(a);
        rootB = Find(b);
        if (!(rootA.equals(rootB))) {
            if (rootA.rank > rootB.rank) {
                rootB.root = rootA;
            } else if (rootB.rank > rootA.rank) {
                rootA.root = rootB;
            } else {
                rootB.root = rootA;
                rootA.rank++;
            }
        }
        count++;
    }
    //start at a random cell(t,z,y,x)
    //select an adjacent cell +- a single index
    //remove wall from newly added cell to dataset

    public void MazeGenn() {
        int t, z, y, x;
        Cell cell0, cell1;
        int index;
        int tsize = this.N;
        Random r = new Random(size - 1);
        boolean move, added;
        int wall0 = 0;
        int wall1 = 0;
        long seed = (long) (Math.random() * 65000);
        r.setSeed(seed);
        Set<Pair<Integer, Boolean>> list = new HashSet();
        while (count < size - 1) {
            t = r.nextInt(tsize);
            x = r.nextInt(tsize);
            y = r.nextInt(tsize);
            z = r.nextInt(tsize);
            cell0 = Alien[t][z][y][x];
            list.clear();
            index = r.nextInt(4);
            move = r.nextBoolean();
            Pair<Integer, Boolean> pair = new Pair(index, move);
            cell1 = cell0;
            added = false;
            while (Find(cell1) == Find(cell0) && list.size() < 8 && !added) {
                //continue adding random values to the set
                //until a new one is placed, add() returns true if inserted
                while (!list.add(pair)) {
                    index = r.nextInt(4);
                    move = r.nextBoolean();
                    pair = new Pair(index, move);
                }

                //0 move -1
                //1 move +1
                //0bttzzyyxx
                //00 bits are +-1 movement in that dimension
                //01 bits are -1 movement in that dimension
                //10 bits are +1 movement in that dimension
                //11 is a wall no movement
                //1 move + 1
                //0 move - 1
                switch (index) {
                    //0 is x movement
                    case 0:
                        if (move && x + 1 <= tsize - 1) {
                            cell1 = Alien[t][z][y][x + 1];
                            wall0 = XR;
                            wall1 = XL;
                        } else if (!move && x - 1 >= 0) {
                            cell1 = Alien[t][z][y][x - 1];
                            wall0 = XL;
                            wall1 = XR;
                        }
                        break;
                    //1 is y movement
                    case 1:
                        if (move && y + 1 <= tsize - 1) {
                            cell1 = Alien[t][z][y + 1][x];
                            wall0 = YR;
                            wall1 = YL;
                        } else if (!move && y - 1 >= 0) {
                            cell1 = Alien[t][z][y - 1][x];
                            wall0 = YL;
                            wall1 = YR;
                        }
                        break;
                    //2 is z movement
                    case 2:
                        if (move && z + 1 <= tsize - 1) {
                            cell1 = Alien[t][z + 1][y][x];
                            wall0 = ZU;
                            wall1 = ZD;
                        } else if (!move && z - 1 >= 0) {
                            cell1 = Alien[t][z - 1][y][x];
                            wall0 = ZD;
                            wall1 = ZU;
                        }
                        break;
                    //3 is t movement
                    case 3:
                        if (move && t + 1 <= tsize - 1) {
                            cell1 = Alien[t + 1][z][y][x];
                            wall0 = TF;
                            wall1 = TB;
                        } else if (!move && t - 1 >= 0) {
                            cell1 = Alien[t - 1][z][y][x];
                            wall0 = TB;
                            wall1 = TF;
                        }
                        break;
                }
                if (!(Find(cell1) == (Find(cell0)))) {
                    this.Union(cell0, cell1);
                    cell0.walls -= wall0;
                    cell1.walls -= wall1;
                    added = true;
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        MazeGen maze = new MazeGen(30);
        maze.MazeGenn();
        FileOutputStream fos = new FileOutputStream("maze.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        int tsize = maze.N;
        for (int time = 0; time < tsize; time++) {
            for (int Z = 0; Z < tsize; Z++) {
                for (int Y = 0; Y < tsize; Y++) {
                    for (int X = 0; X < tsize; X++) {
                        dos.writeByte(maze.Alien[time][Z][Y][X].walls);
                        //zeros = String.format("%8s", Integer.toBinaryString(maze.Alien[time][Z][Y][X].walls)).replace(' ', '0');
                        //hex = Integer.toHexString(maze.Alien[time][Z][Y][X].walls);
                        //System.out.print(hex);
                        //System.out.print(" "+ (char)maze.Alien[time][Z][Y][X].walls);
                        //System.out.println(" " + zeros);
                    }
                }
            }

        }
        dos.close();
    }
}
