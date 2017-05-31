/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazegen;

/**
 *
 * @author Andy
 */
public class Cell {

    public Cell root;
    public int rank;
    public int walls;
    public int[] location;

    
    public Cell(Cell Croot, int Crank, int Cwalls, int[] Clocation) {
        root = Croot;
        rank = Crank;
        walls = Cwalls;
        location = Clocation;
    }

}
