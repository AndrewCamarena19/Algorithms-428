package graycode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Vector;

//000 
//001 
//011 
//010 
//110 
//111 
//101 
//100
/**
 *
 * @author Andy
 */
public class GrayCode {

    static ArrayList<String> ints = new ArrayList();

    public static void gray2(int len) throws IOException {

        FileWriter fw = new FileWriter("Output.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        ints.add("0");
        ints.add("1");
        //start at index 2 of array go until 2^
        for (int i = 2; i < Math.pow(2, len); i = i << 1) {

            //add top of list to bottom reverse
            for (int j = i - 1; j >= 0; j--) {
                ints.add(ints.get(j));
            }
            //concatinate leading 0's to top half of list
            for (int j = 0; j < ints.size() / 2; j++) {
                ints.set(j, "0" + ints.get(j));
            }
            //concatinate 1's to bottom half of list
            for (int j = ints.size() / 2; j < ints.size(); j++) {
                ints.set(j, "1" + ints.get(j));
            }

        }
        for (int i = 0; i < ints.size(); i++) {
            System.out.println(ints.get(i));
        }
        //pw.close();
    }

    public static void gray3(int len) throws IOException {

        FileWriter fw = new FileWriter("gray.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        ints.add("0");
        ints.add("1");
        ints.add("2");
        //start at index 3 of array go until 3^n
        for (int i = 3; i < Math.pow(3, len); i = i * 3) {

            //add top of list to bottom reverse
            for (int j = i - 1; j >= 0; j--) {
                ints.add(ints.get(j));
            }
            for (int j = 0; j < i; j++) {
                ints.add(ints.get(j));
            }
            //concatinate leading 0's to top third
            for (int j = 0; j < ints.size() / 3; j++) {
                ints.set(j, "0" + ints.get(j));
            }
            //concatinate 1's to second third
            for (int j = ints.size() / 3; j < ints.size() * 2 / 3; j++) {
                ints.set(j, "1" + ints.get(j));
            }
            //bottom 3rd gets a 2
            for (int j = ints.size() * 2 / 3; j < ints.size(); j++) {
                ints.set(j, "2" + ints.get(j));
            }
        }
        for (int i = 0; i < ints.size(); i++) {
            pw.println(ints.get(i));
        }
        pw.close();
    }

    public static void gray6(int len) throws IOException {

        FileWriter fw = new FileWriter("gray.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        ints.add("0");
        ints.add("1");
        ints.add("2");
        ints.add("3");
        ints.add("4");
        ints.add("5");
        //start at index 6 of array go until 6^n
        for (int i = 6; i < Math.pow(6, len); i = i * 6) {
        	//magic number
            for (int k = 0; k < 2; k++) {
                //add top of list to bottom reverse
                for (int j = i - 1; j >= 0; j--) {
                    ints.add(ints.get(j));
                }
                for (int j = 0; j < i; j++) {
                    ints.add(ints.get(j));
                }
            }
            for (int j = i - 1; j >= 0; j--) {
                ints.add(ints.get(j));
            }
            for (int j = 0; j < ints.size() / 6; j++) {
                ints.set(j, "0" + ints.get(j));
            }
            //concatinate 1
            for (int j = ints.size() / 6; j < ints.size() * 2 / 6; j++) {
                ints.set(j, "1" + ints.get(j));
            }
            //2
            for (int j = ints.size() * 2 / 6; j < ints.size() * 3 / 6; j++) {
                ints.set(j, "2" + ints.get(j));
            }
            //3
            for (int j = ints.size() * 3 / 6; j < ints.size() * 4 / 6; j++) {
                ints.set(j, "3" + ints.get(j));
            }
            //4
            for (int j = ints.size() * 4 / 6; j < ints.size() * 5 / 6; j++) {
                ints.set(j, "4" + ints.get(j));
            }
            //5
            for (int j = ints.size() * 5 / 6; j < ints.size(); j++) {
                ints.set(j, "5" + ints.get(j));
            }

        }
        for (int i = 0; i < ints.size(); i++) {
            pw.println(ints.get(i));
        }
        pw.close();
    }

    public static void gray9(int len) throws IOException {

        FileWriter fw = new FileWriter("gray.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        ints.add("0");
        ints.add("1");
        ints.add("2");
        ints.add("3");
        ints.add("4");
        ints.add("5");
        ints.add("6");
        ints.add("7");
        ints.add("8");
        //start at index 9 of array go until 9^n
        for (int i = 9; i < Math.pow(9, len); i = i * 9) {

            for (int k = 0; k < 4; k++) {
                for (int j = i - 1; j >= 0; j--) {
                    ints.add(ints.get(j));
                }
                for (int j = 0; j < i; j++) {
                    ints.add(ints.get(j));
                }
            }
            //0
            for (int j = 0; j < ints.size() / 9; j++) {
                ints.set(j, "0" + ints.get(j));
            }
            //1
            for (int j = ints.size() / 9; j < ints.size() * 2 / 9; j++) {
                ints.set(j, "1" + ints.get(j));
            }
            //2
            for (int j = ints.size() * 2 / 9; j < ints.size() * 3 / 9; j++) {
                ints.set(j, "2" + ints.get(j));
            }
            //3
            for (int j = ints.size() * 3 / 9; j < ints.size() * 4 / 9; j++) {
                ints.set(j, "3" + ints.get(j));
            }
            //4
            for (int j = ints.size() * 4 / 9; j < ints.size() * 5 / 9; j++) {
                ints.set(j, "4" + ints.get(j));
            }
            //5
            for (int j = ints.size() * 5 / 9; j < ints.size() * 6 / 9; j++) {
                ints.set(j, "5" + ints.get(j));
            }
            //6
            for (int j = ints.size() * 6 / 9; j < ints.size() * 7 / 9; j++) {
                ints.set(j, "6" + ints.get(j));
            }
            //7
            for (int j = ints.size() * 7 / 9; j < ints.size() * 8 / 9; j++) {
                ints.set(j, "7" + ints.get(j));
            }
            //8
            for (int j = ints.size() * 8 / 9; j < ints.size(); j++) {
                ints.set(j, "8" + ints.get(j));
            }
        }
        for (int i = 0; i < ints.size(); i++) {
            pw.println(ints.get(i));
        }
        pw.close();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        gray6(9);
    }

}
