/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs6591_project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author Suranjan
 *
 */
public class MST_Kruskal {

    ArrayList<points> sites = new ArrayList<points>();  // ArrayList to store the coordinates of the points
    ArrayList<Edge> trafficT = new ArrayList<Edge>();   // ArrayList to store the traffic table 
    ArrayList<Edge> distT = new ArrayList<Edge>();      // ArrayList to store the distances between all combination of nodes
    ArrayList<Edge> mst = new ArrayList<Edge>();        // ArrayList to store the list of Edge of the spanning tree
    private static DecimalFormat df2 = new DecimalFormat(".##");    // To get the decimal format in order
    set components[];   //To store the different components during Kruskal algorithm    
    int parentList[][]; // To store the parent node of all the nodes
    double sum = 0, maxUti, avgHopCount;  // To store the total sum of MST, Maximum utilization and Average Hop count
    String ans = "";

    /**
     * Method to read the node locations and the traffic table files
     *
     * @param fileName1
     * @param fileName2
     * @throws FileNotFoundException
     */
    public void read(String fileName1, String fileName2) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName1));
        String inp[] = new String[3];
        while (sc.hasNextLine()) {
            inp = sc.nextLine().split("\\s");
            // Adding new points.
            sites.add(new points(Integer.parseInt(inp[0]), Integer.parseInt(inp[1]), Integer.parseInt(inp[2])));
        }
        // Initialize the distance table by taking all the combinations
        for (int i = 0; i < sites.size(); i++) {
            for (int j = 0; j < sites.size(); j++) {
                if (i != j) {
                    distT.add(new Edge(i, j, -1, euclideanDist(sites.get(i).getX(), sites.get(i).getY(), sites.get(j).getX(), sites.get(j).getY())));
                }
            }
        }

        sc = new Scanner(new File(fileName2));
        inp = new String[4];
        while (sc.hasNextLine()) {
            inp = sc.nextLine().split("\\s");
            // Initialize the traffic table
            trafficT.add(new Edge(Integer.parseInt(inp[0]), Integer.parseInt(inp[1]), Integer.parseInt(inp[2]),
                    distT.get((Integer.parseInt(inp[0]) - 1) * 10 + (Integer.parseInt(inp[1]) - 1)).getDist()));
        }
        // Sorting the distance array according to edge weights
        Collections.sort(distT, new Comparator<Edge>() {
            @Override
            public int compare(Edge E1, Edge E2) {
                return Double.compare(E1.getDist(), E2.getDist());
            }
        });
        // Initialize the components array, all nodes are independent components at first.
        components = new set[sites.size()];
        for (int i = 0; i < components.length; i++) {
            components[i] = new set();
            components[i].parent = i;   // Every node is its own parent
            components[i].rank = 0;
        }
        //path = new int[trafficT.size()][10];
        //traffic = new int[sites.size()][1];
        parentList = new int[sites.size()][1];
        for (int i = 0; i < sites.size(); i++) {
            parentList[i][0] = -1;
            //  traffic[i][0] = 0;
        }
    }

    /**
     * Method to calculate the euclidean distance between two points
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public double euclideanDist(int x1, int y1, int x2, int y2) {
        return Math.sqrt((y1 - y2) * (y1 - y2) + (x1 - x2) * (x1 - x2));
    }

    /**
     * Method to generate the minimum spanning tree using Kruskal's algorithm
     */
    public void kruskal() {
        for (int i = 0; i < distT.size(); i++) {
            int srcP = findParent(components, distT.get(i).getSource());// Find the parent of source node
            int destP = findParent(components, distT.get(i).getDest()); // Find the parent of destination node
            if (srcP != destP) {    // Both the parents are not equal indicates no loop
                combine(srcP, destP);   // Combining both source and destination as one component
                sum += distT.get(i).getDist();
                System.out.println((distT.get(i).getSource() + 1) + " " + (distT.get(i).getDest() + 1) + " " + df2.format(distT.get(i).getDist()) + "Kms");
                constructTree(distT.get(i).getSource(), distT.get(i).getDest());// Generate the tree using either source as parent of destination or viceversa
                mst.add(new Edge(distT.get(i).getSource(), distT.get(i).getDest(), 0, distT.get(i).getDist()));// Add the new edge to the MST arraylist
            }
        }
        System.out.println("MST Weight= " + df2.format(sum) + '\n');
    }

    /**
     * Combining two components with different parents to a single component by
     * comparing rank of both source and destination.
     *
     * @param src
     * @param dest
     */
    public void combine(int src, int dest) {
        if (components[src].rank > components[dest].rank) {
            components[dest].parent = components[src].parent;
        } else if (components[src].rank < components[dest].rank) {
            components[src].parent = components[dest].parent;
        } else {
            // If both rank are equal, combine the source to the destination and increase the destination rank
            components[src].parent = components[dest].parent;
            components[dest].rank++;
        }
    }

    /**
     * Recursive function to find the root parent of a node.
     *
     * @param subset
     * @param e element for which parent needs to be found
     * @return
     */
    public int findParent(set subset[], int e) {
        if (subset[e].parent == e) {
            return e;
        }
        return findParent(subset, subset[e].parent);
    }

    /**
     * Method to construct a tree given two nodes
     *
     * @param element1
     * @param element2
     */
    public void constructTree(int element1, int element2) {
        // If element1 has no parent. Make element2 its parent else Vice versa.
        if (parentList[element1][0] == -1) {
            parentList[element1][0] = element2;
        } else {
            parentList[element2][0] = element1;
        }
    }

    /**
     * Method to find the path between two given points in a MST
     *
     * @param V1
     * @param V2
     * @return
     */
    public String findPath(int V1, int V2) {
        String p1 = "" + V1, p2 = "" + V2;
        int common1 = 0, common2 = 0;
        // Iterating till we find the root node from Node 1
        while (parentList[V1][0] != -1) {
            V1 = parentList[V1][0];
            p1 += V1;
        }
        // Iterating till we reach the root node from Node2
        while (parentList[V2][0] != -1) {
            V2 = parentList[V2][0];
            p2 += V2;
        }
        // Find the first common ancestor
        for (int i = 0; i < p1.length(); i++) {
            if (p2.contains("" + p1.charAt(i))) {
                common1 = i;
                break;
            }
        }
        common2 = p2.indexOf(p1.charAt(common1));
        // Take the path from Node 1 to first common ancestor. Then from first common ancestor till Node 2.
        String path = p1.substring(0, common1) + new StringBuilder((p2).substring(0, common2 + 1)).reverse().toString();
        return path;
    }

    /**
     * Method to find traffic on each link in the spanning tree
     */
    public void trafficOnEachLink() {
        String path;
        // Finding the path between two nodes
        for (int i = 0; i < trafficT.size(); i++) {
            path = findPath((trafficT.get(i).getSource() - 1), (trafficT.get(i).getDest() - 1));
            //Traversing the path to keep adding traffic on each link
            for (int j = 0; j < path.length() - 1; j++) {
                updateTraffic(Character.getNumericValue(path.charAt(j)), Character.getNumericValue(path.charAt(j + 1)), trafficT.get(i).getTraffic());
            }
        }
        Double uti; // To store the utilization
        maxUti = 0.0; // To get the max utilization
        Double sum = 0.0;// To store the sum of all utilizations
        for (int i = 0; i < mst.size(); i++) {
            // Getting the utilization
            uti = (double) ((double) (mst.get(i).getTraffic()) / (double) 1544) * 100;
            if (uti > maxUti) {
                maxUti = uti;
            }
            sum += uti;
            System.out.println((mst.get(i).getSource() + 1) + "  " + (mst.get(i).getDest() + 1) + "  " + mst.get(i).getTraffic() + " Kbps " + df2.format(uti) + "%");
        }

        System.out.println("Max Utilization " + df2.format(maxUti));
        System.out.println("Average Utilization " + df2.format(sum / mst.size()) + '\n');
    }

    /**
     * Method to find the node from MST and update the traffic on each link
     *
     * @param src
     * @param dest
     * @param traff
     */
    public void updateTraffic(int src, int dest, int traff) {
        for (int i = 0; i < mst.size(); i++) {
            if ((mst.get(i).getSource() == src && mst.get(i).getDest() == dest) || (mst.get(i).getSource() == dest && mst.get(i).getDest() == src)) {
                mst.get(i).setTraffic(mst.get(i).getTraffic() + traff);
            }
        }
    }

    /**
     * Function to get the traffic in an edge in the MST.
     *
     * @param src
     * @param dest
     * @return
     */
    public int getTrafficBtnNode(int src, int dest) {
        for (int i = 0; i < mst.size(); i++) {
            if ((mst.get(i).getSource() == src && mst.get(i).getDest() == dest) || (mst.get(i).getSource() == dest && mst.get(i).getDest() == src)) {
                return mst.get(i).getTraffic();
            }
        }
        return 0;
    }

    /**
     * Method to calculate the total traffic between any two given nodes
     *
     * @param a
     * @param b
     * @return
     */
    public int totalTrafficBetweenTwoNodes(int a, int b) {
        String path = findPath(a, b);
        int totalTraffic = 0;
        // iterate over the path
        for (int i = 0; i < path.length() - 1; i++) {
            // Keep adding the total traffic between each path.
            totalTraffic += getTrafficBtnNode(Character.getNumericValue(path.charAt(i)), Character.getNumericValue(path.charAt(i + 1)));
        }
        return totalTraffic;
    }

    /**
     * Method to calculate the average hop count
     */
    public void avgHopCount() {
        int traffic = 0;  // Store the traffic from a given edge
        int totTraff = 0; // Store the total traffic between every possible node values 
        int trafficHop = 0;// Store the total traffic*hop between all pairs of node values
        String path = "";
        Double avgHop;// To get the average hop count
        // Loop through all the sites
        for (int i = 0; i < sites.size(); i++) {
            for (int j = i + 1; j < sites.size(); j++) {
                traffic = totalTrafficBetweenTwoNodes(i, j);
                path = findPath(i, j);
                totTraff += traffic;// Keep adding the over all traffic
                trafficHop += traffic * (path.length() - 1);//Keep adding traffic*hop for all node pairs
                System.out.println((i + 1) + "  " + (j + 1) + "  " + traffic + " Kbps " + (path.length() - 1) + " hops");
            }
        }
        avgHop = (double) ((double) trafficHop / (double) totTraff);
        System.out.println("Average Hops = " + df2.format(avgHop));
        System.out.println("TBar= 0.0054 sec: Average Hops = " + df2.format(avgHop) + " hops: Max Utilization= " + df2.format(maxUti) + '\n');
        System.out.println("Average Delay is " + df2.format((0.0054 * avgHop) / (1 - (maxUti / 100))) + " secs");
    }
}
