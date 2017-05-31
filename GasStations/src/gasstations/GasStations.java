/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gasstations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andy
 */
public class GasStations {

    static ConcurrentHashMap<Integer, Vertex> verts = new ConcurrentHashMap();
    static ConcurrentHashMap<Integer, Vertex> solCheck = new ConcurrentHashMap();
    static ConcurrentHashMap<Integer, Vertex> copy;
    static Map<Integer, Set<Integer>> set = new HashMap();
    static Set<Integer> visited = new HashSet();
    static Set<Integer> subset = new HashSet();
    static Set<Integer> vertices = new HashSet();
    static Set<Integer> removelist = new HashSet();
    public static Integer big = 0;
    static Integer limit;

    public static class Vertex {

        Integer Vertex;
        ConcurrentHashMap<Integer, Integer> cVertices = new ConcurrentHashMap();

        @Override
        public String toString() {
            return Vertex + ": " + cVertices.toString();
        }
    }

    public static ConcurrentHashMap<Integer, Vertex> read() {
        // Open the file
        ConcurrentHashMap<Integer, Vertex> list = new ConcurrentHashMap();
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("network.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GasStations.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        Vertex curr;
        ConcurrentHashMap<Integer, Integer> temp;
        try (
                //Read File Line By Line
                //String vertex = br.readLine();
                //remove newlines and commas
                Scanner cin = new Scanner(br)) {
            limit = cin.nextInt();
            String parse = "";
            //cin.useDelimiter(",|:");
            cin.nextLine();
            while (cin.hasNext()) {
                curr = new Vertex();
                String rest = cin.nextLine().replace(":", " ");
                rest = rest.replace(",", " ");
                rest = rest.replace("[", " ");
                rest = rest.replace("]", " ");
                String[] tokens = rest.split(" ");
                temp = new ConcurrentHashMap();
                curr.Vertex = Integer.parseInt(tokens[0]);
                for (int i = 2; i < tokens.length; i = i + 3) {
                    temp.put(Integer.parseInt(tokens[i]), Integer.parseInt(tokens[i + 1]));
                }
                curr.cVertices = temp;
                list.put(curr.Vertex, curr);
            }
        }

        try {
            //Close the input stream
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(GasStations.class.getName()).log(Level.SEVERE, null, ex);
        }
        //change = new ArrayList<>(sorty(list));

        return (list);
    }

    public static void traverse(Vertex v, int distance, int prev) {
        Set<Integer> subs = new HashSet();
        if (v != null) {
            for (Map.Entry<Integer, Integer> entry : v.cVertices.entrySet()) {
                //copy.get(entry.getKey()).cVertices.remove(v.Vertex);
                //subs.add(entry.getKey());
                if (distance + entry.getValue() <= limit && entry.getKey() != prev) {
                    traverse(verts.get(entry.getKey()), distance + entry.getValue(), v.Vertex);
                    removelist.add(entry.getKey());
                    vertices.add(entry.getKey());
                    subs.add(entry.getKey());
                }
            }
            subset.addAll(subs);
        }
    }

    public static void traverse2(Vertex v, int distance, int prev) {
        Set<Integer> subs = new HashSet();
        if (v != null) {
            for (Map.Entry<Integer, Integer> entry : v.cVertices.entrySet()) {
                //copy.get(entry.getKey()).cVertices.remove(v.Vertex);
                //subs.add(entry.getKey());
                if (distance + entry.getValue() <= limit && entry.getKey() != prev) {
                    traverse2(verts.get(entry.getKey()), distance + entry.getValue(), v.Vertex);
                    vertices.add(entry.getKey());
                    subs.add(entry.getKey());
                }
            }
            subset.addAll(subs);
        }
    }

    static public void preCheck(int start) {
        verts = read();
        for (Map.Entry<Integer, Vertex> entry : verts.entrySet()) {
            for (Map.Entry<Integer, Integer> x : entry.getValue().cVertices.entrySet()) {
                if (x.getValue() > limit) {
                    verts.get(entry.getKey()).cVertices.remove(x.getKey());
                }
            }
        }
        //return check(start);
    }

    static public void travhelp(int vert) {
        Vertex max2 = null;
        max2 = verts.get(vert);
        traverse2(max2, 0, max2.Vertex);
        subset.add(max2.Vertex);
        set.put(max2.Vertex, subset);
        //System.out.println(subset.size());
        vertices.add(max2.Vertex);
        verts.remove(max2.Vertex);
        subset = new HashSet();
        for (Integer x : removelist) {
            verts.remove(x);
        }
        removelist = new HashSet();
    }

    static public Set<Integer> check(int start) {
        if (start == 0) {
            vertices = new HashSet();
            verts = read();
        }
        Vertex max2 = null;

        Map.Entry<Integer, Set<Integer>> max = null;
        //int big = 0;
        int count = 0;
        int curr = 0;
        Vertex value;
        //node centers produced by random walks
        //remove all incident nodes produce independent sets
        //creates ~150 nodes
        while (!verts.isEmpty()) {
            Random gen = new Random();
            Object[] values = verts.values().toArray();
            count = gen.nextInt(values.length);
            value = (Vertex) values[count];
            max2 = value;
            traverse(max2, 0, max2.Vertex);
            subset.add(max2.Vertex);
            set.put(max2.Vertex, subset);
            vertices.add(max2.Vertex);
            verts.remove(max2.Vertex);
            for (Integer x : removelist) {
                verts.remove(x);
            }
            removelist = new HashSet();
            if (subset.size() > curr) {
                curr = subset.size();
                big = max2.Vertex;
            }
            subset = new HashSet();
            if (verts.isEmpty()) {
                break;
            }

            //System.out.println(count);
        }
        Set<Integer> master = new HashSet();
        count = 0;
        while (true) {
            max = null;
            for (Map.Entry<Integer, Set<Integer>> entry : set.entrySet()) {
                if (max == null || entry.getValue().size() > max.getValue().size()) {
                    max = entry;
                }
            }
            if (set.isEmpty() || max.getValue().isEmpty()) {
                break;
            }
            for (Map.Entry<Integer, Set<Integer>> entry : set.entrySet()) {
                if (entry.getKey() != max.getKey()) {
                    entry.getValue().removeAll(max.getValue());
                }
                //max.getValue().removeAll(entry.getValue());
                //System.out.println(max.getValue().toString());
                //System.out.println();
                //System.out.println(entry.getValue().toString());
            }
            count++;
            master.add(max.getKey());
            set.remove(max.getKey());
        }
        //if size == 10648 all vertices visited
        if (vertices.size() == 10648) {
            System.out.println(big + " vertex has: " + curr);
            return master;
        } else {
            return null;
        }
    }

    static public Set<Integer> checkfin(Set<Integer> tempmaster) {
        Vertex max2 = null;
        vertices = new HashSet();
        verts = read();
        set = new HashMap();
        Map.Entry<Integer, Set<Integer>> max = null;
        int big = 0;
        int count = 0;
        int curr = 0;
        Vertex value;
        for (Integer x : tempmaster) {
            Vertex entry = verts.get(x);
            traverse2(entry, 0, entry.Vertex);
            subset.add(entry.Vertex);
            set.put(entry.Vertex, subset);
            vertices.add(entry.Vertex);
            System.out.println("Current vertex: " + x.toString() + " covers " + subset.size());
            subset = new HashSet();
            if (verts.isEmpty()) {
                break;
            }
        }
        Set<Integer> master = new HashSet();
        count = 0;
        while (true) {
            max = null;
            for (Map.Entry<Integer, Set<Integer>> entry : set.entrySet()) {
                if (max == null || entry.getValue().size() > max.getValue().size()) {
                    max = entry;
                }
            }
            if (set.isEmpty() || max.getValue().isEmpty()) {
                break;
            }
            for (Map.Entry<Integer, Set<Integer>> entry : set.entrySet()) {
                if (entry.getKey() != max.getKey()) {
                    entry.getValue().removeAll(max.getValue());
                }
                //max.getValue().removeAll(entry.getValue());
                //System.out.println(max.getValue().toString());
                //System.out.println();
                //System.out.println(entry.getValue().toString());
            }
            count++;
            master.add(max.getKey());
            set.remove(max.getKey());
        }
        if (vertices.size() == 10648) {
            // System.out.println(big + " vertex has: " + curr);
            return master;
        } else {
            return null;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Set<Integer> currMaster = new HashSet();
        Set<Integer> tempMaster = new HashSet();
        //random walks keeping best result for largest node traversal
        //nodes in first list will have overlap ~100 nodes
        //use those sets of nodes to create a new set of nodes that cover all graph
        GasStations g1;
        g1 = new GasStations();
        currMaster = g1.check(0);
        for (int i = 0; i < 10; i++) {
            g1 = new GasStations();
            tempMaster = g1.check(0);
            if (tempMaster != null && tempMaster.size() < currMaster.size()) {
                currMaster = tempMaster;
            }
            tempMaster = new HashSet();
            System.out.println("Current smallest: " + currMaster.size());
        }
        //recreate a unique set of nodes with unique adjacent nodes
        //dfs with new set of nodes that i know cover map
        //only have to traverse ~100 nodes
        //union and remove from sets
        for (Integer x : checkfin(currMaster)) {
            System.out.print(x.toString() + 'x');
        }
    }
}
