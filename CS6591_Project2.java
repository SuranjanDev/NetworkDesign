/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs6591_project2;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author Suranjan
 */
public class CS6591_Project2 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
     MST_Kruskal mst=new MST_Kruskal();
     mst.read("NodeLocations.txt","Traffic.txt");
     mst.kruskal();
     mst.trafficOnEachLink();
     mst.avgHopCount();
    }
}
