/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornetwork;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author Andy
 */
public class SensorNetwork {

    static Double Distance;
    public static ArrayList<Point2D.Double> read() {
        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("sensors.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SensorNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        Double x;
        Double y;
        String distance;
        Point2D.Double start = null;
        ArrayList<Point2D.Double> list = new ArrayList<>();
        Double d;
        try {
            //Read File Line By Line
            distance = br.readLine();
            Distance = Double.parseDouble(distance);
            System.out.println(distance);
            //remove newlines and commas
            Scanner cin = new Scanner(br);
            cin.useDelimiter(",|\\n");
            //put points into Points2D.Double arraylist
            while (cin.hasNext()) {
                x = cin.nextDouble();
                y = cin.nextDouble();
                start = new Point2D.Double(x, y);
                list.add(start);
            }

        } catch (IOException ex) {
            Logger.getLogger(SensorNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            //Close the input stream
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(SensorNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ArrayList<Point2D.Double> sort(ArrayList<Point2D.Double> list) {
        //sort the points in order from distance about the origin
        Collections.sort(list, (Point2D.Double p1, Point2D.Double p2) -> Double.compare(p1.distance(0.0, 0.0), p2.distance(0.0, 0.0)));
        return list;
    }

    public static ArrayList<Point2D.Double> test1(ArrayList<Point2D.Double> vert) {
        Point2D.Double watch1;
        Point2D.Double watch2;
        Double watch3;
        ArrayList<Point2D.Double> temp;
        ArrayList<Point2D.Double> big = null;
        int count;
        int index;
        for (int i = 0; i < vert.size(); i++) {
            count = 0;
            index = 0;
            watch1 = vert.get(i);
            temp = new ArrayList<>(vert);
            for (int k = 0; k < temp.size(); k++) {
                watch2 = temp.get(k - count);
                watch3 = temp.get(k - count).distance(vert.get(i));
                if (watch3 > Distance) {
                    //if a point is ever farther than max distance
                    //all following points will be also since it is sorted
                    //temp.remove(k - count);
                    count++;
                    break;
                }
                index++;
            }
            //if a newly generated array sublist is largest than the previously stored big array
            //a larger array was found
            if (big == null || temp.subList(i, index+i).size() > big.size()) {
                big = new ArrayList<>(temp.subList(i, index+i));
                System.out.println("Found a bigger One");
            }
            //Sanity check, 26min on 1million entries?
            //System.out.println(i);
        }
        return big;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Point2D.Double a = new Point2D.Double(654.651,8798.351);
        //Point2D.Double b = new Point2D.Double(650.651,8798.351);
        //System.out.println(a.distance(b));
        ArrayList<Point2D.Double> verts = read();
        ArrayList<Point2D.Double> sorted = sort(verts);
        ArrayList<Point2D.Double> nvert = test1(sorted);
        System.out.println("List of points in largest clique");
        for (Point2D.Double v : nvert)
            System.out.println(v.x + ", " + v.y);
    }

}
