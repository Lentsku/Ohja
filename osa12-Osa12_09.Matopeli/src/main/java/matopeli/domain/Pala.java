/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matopeli.domain;

/**
 *
 * @author lamiika
 */
public class Pala {
    private int x;
    private int y;
    
    public Pala(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public boolean osuu(Pala pala) {
        if (pala.getX() == this.x && pala.getY() == this.y) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
