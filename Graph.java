/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs6591_project2;

/**
 *
 * @author Suranjan
 */
public class Graph {


    int V, E;
    Edge edge[];
   public Graph(int vertices, int edges){
       this.V=vertices;
       this.E=edges;
       
       edge=new Edge[E];
       for(int i=0;i<E;i++)
           edge[i]=new Edge();
   }

}
