/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warp;

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
public class Warp {

    public int gates;
    public double gdistance;

    public ArrayList<Point> read(String text) {
        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(text);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Warp.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        double x;
        double y;
        double z;
        String distance;
        Point start = null;
        double Distance;
        ArrayList<Point> list = new ArrayList<>();
        Double d;
        Scanner cin = new Scanner(br);
        if (text.equalsIgnoreCase("galaxy.txt")) {
            gates = cin.nextInt();
            gdistance = cin.nextDouble();
        }
        cin.useDelimiter(",|\\n");
        while (cin.hasNext()) {
            x = cin.nextDouble();
            y = cin.nextDouble();
            z = cin.nextDouble();
            start = new Point(x, y, z);
            list.add(start);
        }

        try {
            //Close the input stream
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Warp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (list);
    }

    public ArrayList<Point> sort(ArrayList<Point> list) {
        //sort the points in order from distance about the origin
        Collections.sort(list, (Point p1, Point p2) -> Double.compare(p1.pdistance(new Point(0, 0, 0)), p2.pdistance(new Point(0, 0, 0))));
        return list;
    }

    //3d point class
    //distance formula same as 4d
    //brute force approach
    //pick points that satisfy limit condition
    //if limit condition gets too big find nearest warpgate within current limit, increment until reached
    //pick farthest warpgate from current position (if distance > limit) limit == 1000, limit -= 10
    //repeat
    public class Point {

        private final double x, y, z;

        public Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        //check distance
        public double pdistance(Point b) {
            return Math.sqrt(Math.pow((this.x - b.x), 2) + Math.pow((this.y - b.y), 2) + Math.pow((this.z - b.z), 2));
        }

        @Override
        public String toString() {
            return this.x + "," + this.y + "," + this.z;
        }
    }

    public void test() {

        ArrayList<Point> galaxy = read("galaxy.txt");
        ArrayList<Point> holes = read("wormholes.txt");
        Point p = galaxy.get(0);
        Point w = holes.get(0);
        int limit = 100;
        ArrayList<Point> points = new ArrayList();
        ArrayList<ArrayList<Point>> clique = new ArrayList();
        int x = 0;
        try {
            do {
            	//if the entire list is traversed and no point found within limit
                //increment limit by 10 until one is found
                //reset x to start from beginining of array
                if (x == galaxy.size()) {
                    x = 0;
                    clique.add(points);
                    points = new ArrayList();
                    p = galaxy.get(0);
                    //limit += 10;
                }
                //pick the first point that satisfies limit condition from point p
                //remove that point from possible points
                //set it as new point p
                //if a point is found return limit to initial condition 10
                if (p.pdistance(galaxy.get(x)) < limit) {
                    //distances.add(p.pdistance(galaxy.get(x)));
                    p = galaxy.get(x);
                    points.add(p);
                    galaxy.remove(x);
                    System.out.println(galaxy.size());
                    //limit = 10;
                } else {
                    x++;
                }
                //keep goin until list is empty
            } while (!galaxy.isEmpty());
            int count = 0;
            for(ArrayList<Point> b : clique)
                if(b.size() > 34) 
                {
                    //count++;
                    for(int i = 0; i < holes.size(); i++)
                    {
                        Point mid = b.get(b.size()/2);
                        if(mid.pdistance(holes.get(i))-12.680258263448533 < 51)
                        {
                            System.out.println(holes.get(i));
                            holes.remove(i);
                            count++;
                        }
                    }
                }
            System.out.println(count);
            //distances.sort(null);
            //for(int i = distances.size() - 50; i < distances.size(); i++)
            //    System.out.println(distances.get(i));

        } catch (IndexOutOfBoundsException e) {
            System.out.println("it broke");
        }
        //System.out.println(count);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Warp c = new Warp();
        c.test();

    }

}
