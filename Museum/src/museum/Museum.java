/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
    // Java Program to print Vertex Cover of a given undirected graph
import java.io.*;
import static java.lang.Math.abs;
import java.util.*;
import java.util.LinkedList;
import java.util.stream.Stream;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Andy
 */
public class Museum {

    static Map<Integer, Vertex> map = new HashMap();
    static Set<ArrayList<Integer>> set = new HashSet();
    static Set<Integer> VertexCover = new HashSet();
    static ArrayList<Vertex> list = new ArrayList();
    static Map<Integer, Vertex> Total = new HashMap();
    static Map<Integer, Vertex> temp = new ConcurrentHashMap();

    public static class Vertex {

        Integer Vertex;
        ArrayList<Integer> cVertices = new ArrayList<>();
        int Parition = 0;
        boolean wasvisited = false;

        @Override
        public String toString() {
            return Vertex + ": " + cVertices.toString();
        }
    }

    public static ArrayList<Vertex> sort(ArrayList<Vertex> list) {
        //sort the points in order from distance about the origin
        Collections.sort(list, (Vertex p1, Vertex p2) -> Integer.compare(p1.Vertex, p2.Vertex));
        return list;
    }

    public static ArrayList<ArrayList<Integer>> sorty(ArrayList<ArrayList<Integer>> list) {
        //sort the points in order from distance about the origin
        Collections.sort(list, (ArrayList<Integer> p1, ArrayList<Integer> p2) -> Integer.compare(p1.get(2), p2.get(2)));
        return list;
    }

    public static ArrayList<Vertex> read() {
        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("graph.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        Vertex curr;
        try (
                //Read File Line By Line
                //String vertex = br.readLine();
                //remove newlines and commas
                Scanner cin = new Scanner(br)) {
            String parse = "";
            cin.useDelimiter(",|:");
            Integer next = cin.nextInt();
            while (cin.hasNext()) {

                curr = new Vertex();
                curr.Vertex = next;
                while (cin.hasNext()) {
                    parse = cin.next();
                    if (parse.contains("x")) {
                        break;
                    }
                    curr.cVertices.add(Integer.parseInt(parse));
                }
                parse = parse.replace('x', ' ');
                String[] tokens = parse.split(" ");
                curr.cVertices.add(Integer.parseInt(tokens[0]));
                list.add(curr);
                if (tokens.length > 1) {
                    next = Integer.parseInt(tokens[1]);
                }
            }
        }

        try {
            //Close the input stream
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
        }
        //change = new ArrayList<>(sorty(list));
        for (Vertex v : list) {
            temp.put(v.Vertex, v);
        }
        return sort(list);
    }

    public static boolean isLeaf(Vertex x) {
        return x.cVertices.size() < 2;
    }

    public static boolean hasLeaf(Vertex x) {
        int leaflist = 0;
        for (Integer v : x.cVertices) {
            if (map.get(v).cVertices.size() == 1) {
                leaflist++;
                temp.remove(v);
            }
        }
        return leaflist > 0;
    }

    public static void RemoveVertex(Vertex v) {
        for (Integer x : v.cVertices) {
            map.get(x).cVertices.remove(v.Vertex);
            if (map.get(x).cVertices.isEmpty()) {
                map.remove(x);
                list.get(x).Parition++;
                temp.remove(x);
            }
        }
        map.remove(v.Vertex);
    }

    public static void minCover(ArrayList<Vertex> verts) throws IOException {
        //iterate through list of vertices in decreasing edge order
        //take first vertex print it
        //go to the index of the vertices in the connected list
        //remove first vertex from lists
        //if connected vertex == 0 remove that index
        FileWriter fw = new FileWriter("cover.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        int count = 0;
        Map<Integer, Integer> conn = new HashMap();
        for (Vertex x : verts) {
            map.put(x.Vertex, x);
        }

        boolean exists = false;
        boolean exists2 = false;
        Integer vertex = 0;
        Integer vertex2;
        int size = 0;
        int curr = 0;
        ArrayList<Integer> temp2 = new ArrayList();
        Map.Entry<Integer, Vertex> max = null;
        for (Map.Entry<Integer, Vertex> entry : map.entrySet()) {
            if (max == null || entry.getValue().cVertices.size() < max.getValue().cVertices.size()) {
                max = entry;
            }
        }
        temp = new ConcurrentHashMap(map);
        while (!temp.isEmpty()) {
            max = null;
            for (Map.Entry<Integer, Vertex> entry : map.entrySet()) {
                if (max == null || entry.getValue().cVertices.size() > max.getValue().cVertices.size()) {
                    max = entry;
                }
            }
            temp2.add(max.getKey());
            RemoveVertex(max.getValue());
            list.get(max.getKey()).Parition++;
            temp.remove(max.getKey());

            //System.out.println(map.size());
            for (Vertex b : list) {
                if (b.Parition != 1) {
                    count++;
                }
            }
            //System.out.println(count);

        }
        for (Integer x : temp2) {
            System.out.println(x.toString() + 'x');
        }
    }

    public static class compar implements Comparator<ArrayList<Integer>> {

        @Override
        public int compare(ArrayList<Integer> x, ArrayList<Integer> y) {
            // Assume neither string is null. Real code should
            // probably be more robust
            // You could also just return x.length() - y.length(),
            // which would be more efficient.
            if (x.get(1) < y.get(1)) {
                return -1;
            }
            if (x.get(1) > y.get(1)) {
                return 1;
            }
            return 0;
        }
    }

    public static void bipart(ArrayList<Vertex> verts) {
        Set<ArrayList<Integer>> set = new HashSet();
        ArrayList<Integer> temp = new ArrayList();
        Set<Integer> s = new HashSet();
        Set<Integer> u = new HashSet();
        for (Vertex v : verts) {
            map.put(v.Vertex, v);
        }
        int count = 0;
        Vertex b = verts.get(0);
        int index = 0;
        Map<Integer, Vertex> remain = new HashMap();
        for (Map.Entry<Integer, Vertex> entry : map.entrySet()) {
            remain.put(entry.getKey(), entry.getValue());
        }
        int part = 0;
        boolean changed = true;
        map.get(b.Vertex).Parition = 1;
        remain.remove(b.Vertex);
        while (!remain.isEmpty()) {
            if (!changed) {
                index = remain.values().iterator().next().Vertex;
                remain.remove(0);
                if (part == 1) {
                    map.get(index).Parition = 2;
                }
                if (part == 2) {
                    map.get(index).Parition = 1;
                }
                changed = true;
            }
            if (b.Parition == 1 || b.Parition == 0) {
                for (Integer x : b.cVertices) {
                    if (map.get(x).Parition == 0) {
                        map.get(x).Parition = 2;
                        index = x;
                        remain.remove(index);
                        changed = true;
                        part = 2;
                    } else {
                        changed = false;
                    }
                }
                b = map.get(index);
            } else if (b.Parition == 2 || b.Parition == 0) {
                for (Integer x : b.cVertices) {
                    if (map.get(x).Parition == 0) {
                        map.get(x).Parition = 1;
                        index = x;
                        changed = true;
                        part = 1;
                        remain.remove(index);
                    } else {
                        changed = false;
                    }
                }
                b = map.get(index);
            }
        }
        System.out.println("Check");
        for (Vertex v : map.values()) {
            System.out.println(v.Vertex + ":" + v.Parition);
        }
    }

    public static void setmin(ArrayList<Vertex> verts) throws IOException {
        ArrayList<Integer> temp = new ArrayList();
        ArrayList<Integer> temp2 = new ArrayList();
        for (Vertex v : verts) {
            for (Integer x : v.cVertices) {
                temp.add(x);
                temp.add(v.Vertex);
                temp.sort(null);
                set.add(temp);
                temp = new ArrayList();
            }
        }
        Set<Integer> setcopy = new HashSet();
        //for (ArrayList<Integer> x : set) {
        //    System.out.println(x.toString());
        // }
        boolean removedA = false;
        boolean removedB = false;
        Map<Integer, Integer> sizes = new HashMap();
        ArrayList<ArrayList<Integer>> setarr = new ArrayList();
        for (ArrayList<Integer> x : set) {
            setarr.add(x);
        }
        System.out.println("Min cover Done");
        //VertexCover.addAll(VC(setarr));
        //System.out.println("First VC done");
        minCover(list);

    }

    public static ArrayList<ArrayList<Integer>> RemoveVert(ArrayList<ArrayList<Integer>> set, Integer vertex) {
        Set<ArrayList<Integer>> toRemove = new HashSet();
        for (ArrayList<Integer> x : set) {
            if (Objects.equals(x.get(0), vertex) || Objects.equals(x.get(1), vertex)) {
                toRemove.add(x);

            }
        }

        set.removeAll(toRemove);
        return set;
    }

    public static Set<Integer> VC(ArrayList<ArrayList<Integer>> set) {
        Integer edge;
        Set<Integer> vertices = new HashSet();
        Set<Integer> left = new HashSet();
        Set<Integer> right = new HashSet();
        Set<Integer> copy = new HashSet();
        while (!set.isEmpty()) {
            //edge = new ArrayList();
            //Collections.shuffle(set);
            edge = set.iterator().next().get(0);
            copy.add(edge);
            //vertices.add(edge.get(1));
            //right.add(edge.get(0));
            //left.add(edge.get(1));
            set = RemoveVert(set, edge);
        }
        return copy;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ArrayList<Vertex> l = read();

        setmin(l);

    }

}
