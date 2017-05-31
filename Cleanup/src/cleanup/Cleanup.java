/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cleanup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andy
 */
public class Cleanup {

    public ArrayList<Point> read() {
        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("points.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cleanup.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        double x;
        double y;
        double z;
        double t;
        String distance;
        Point start = null;
        double Distance;
        ArrayList<Point> list = new ArrayList<>();
        Double d;
        Scanner cin = new Scanner(br);
        cin.useDelimiter(",|\\n");
        while (cin.hasNext()) {
            x = cin.nextDouble();
            y = cin.nextDouble();
            z = cin.nextDouble();
            t = cin.nextDouble();
            start = new Point(x, y, z, t);
            list.add(start);
        }

        try {
            //Close the input stream
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Cleanup.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    //4d point class
    //4d euclidean distance formula
    //sort points from distance to origin to decrease runtime?
    //closer points will be adjacent in list traversal?
    //similar to clique problem?
    //create cliques, travel all points within a clique, travel to nearest clique, repeat?
    //brute force just pick near points?
    public class Point {

        private final double x, y, z, t;

        public Point(double x, double y, double z, double t) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.t = t;
        }
        //check distance
        public double pdistance(Point b) {
            return Math.sqrt(Math.pow((this.x - b.x), 2) + Math.pow((this.y - b.y), 2) + Math.pow((this.z - b.z), 2) + Math.pow((this.t - b.t), 2));
        }

        @Override
        public String toString() {
            return this.x + "," + this.y + "," + this.z + "," + this.t;
        }
    }

    public void test() {

        ArrayList<Point> farray = read();
        Point p = farray.get(0);
        int x = 0;
        int limit = 10;
        try {
            do {
            	//if the entire list is traversed and no point found within limit
            	//increment limit by 10 until one is found
            	//reset x to start from beginining of array
                if (x == farray.size()) {
                    x = 0;
                    limit += 10;
                }
                //pick the first point that satisfies limit condition from point p
                //remove that point from possible points
                //set it as new point p
                //if a point is found return limit to initial condition 10
                if (p.pdistance(farray.get(x)) < limit) {
                    System.out.println(farray.get(x).toString());
                    p = farray.get(x);
                    farray.remove(x);
                    limit = 10;
                } else {
                    x++;
                }
                //keep goin until list is empty
            } while (!farray.isEmpty());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("it broke");
        }
        //System.out.println(count);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Cleanup c = new Cleanup();
        c.test();

    }

}
