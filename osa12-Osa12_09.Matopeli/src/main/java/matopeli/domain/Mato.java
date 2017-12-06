/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matopeli.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lamiika
 */
public class Mato {
    private List<Pala> palalista;
    private int paaX;
    private int paaY;
    private Suunta suunta;
    private boolean poistetaanko;
    
    public Mato(int alkuX, int alkuY, Suunta alkusuunta) {
        this.palalista = new ArrayList<>();
        this.paaX = alkuX;
        this.paaY = alkuY;
        this.suunta = alkusuunta;
        this.poistetaanko = true;
        
        this.palalista.add(new Pala(alkuX, alkuY));
    }
    
    public Suunta getSuunta() {
        return this.suunta;
    }
    
    public void setSuunta(Suunta suunta) {
        this.suunta = suunta;
    }
    
    public int getPituus() {
        return this.palalista.size();
    }
    
    public List<Pala> getPalat() {
        return this.palalista;
    }
    
    public void liiku() {
        if (this.getPituus() < 3) {
            this.kasva();
        }
        
        if (this.poistetaanko == true) {
            this.palalista.remove(0);
        }
        
        if (this.poistetaanko == false) {
            this.poistetaanko = true;
        }
        
        if (this.suunta.ordinal() == Suunta.YLOS.ordinal()) {
            this.paaY--;
        } else if (this.suunta.ordinal() == Suunta.ALAS.ordinal()) {
            this.paaY++;
        } else if (this.suunta.ordinal() == Suunta.VASEN.ordinal()) {
            this.paaX--;
        } else if (this.suunta.ordinal() == Suunta.OIKEA.ordinal()) {
            this.paaX++;
        }
        
        Pala pala = new Pala(this.paaX, this.paaY);
        this.palalista.add(pala);
    }
    
    public void kasva() {
        this.poistetaanko = false;
    }
    
    public boolean osuu(Pala pala) {
        for (int i = 0; i < this.getPituus(); i++) {
            if (this.palalista.get(i).osuu(pala)) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean osuuItseensa() {
        for (int i = 0; i < this.getPituus() - 1; i++) {
            Pala madonPaa = this.palalista.get(this.getPituus() - 1);
            Pala madonOsa = this.palalista.get(i);
            
            if (madonPaa.osuu(madonOsa)) {
                return true;
            }
        }
        
        return false;
    }
}
