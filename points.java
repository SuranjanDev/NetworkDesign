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
public class points {
    private int name;
    private int X;
    private int Y;
    public points(int n,int X,int Y){
        this.name=n;
        this.X=X;
        this.Y=Y;
    }
    public int getName(){
        return this.name;
    }
    public int getX(){
        return this.X;
    }
    public int getY(){
        return this.Y;
    }
}
