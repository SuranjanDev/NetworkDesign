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
    class Edge{
        private int source, destination, traffic;
        private double dist;
    public Edge(int s,int d, int t,double w){
        this.source=s;
        this.destination=d;
        this.traffic=t;
        this.dist=w;
    }
    public Edge(){}
    public int getSource(){
        return this.source;
    }
    public int getDest(){
        return this.destination;
    }
    public int getTraffic(){
        return this.traffic;
    }
    public double getDist(){
        return this.dist;
    }
    public void setTraffic(int traff){
        this.traffic=traff;
    }
    }
