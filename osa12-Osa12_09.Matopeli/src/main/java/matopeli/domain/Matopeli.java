/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matopeli.domain;

import java.util.List;
import java.util.Random;

/**
 *
 * @author lamiika
 */
public class Matopeli {
    
    private int leveys;
    private int korkeus;
    private Mato mato;
    private Omena omena;

    public Matopeli(int leveys, int korkeus) {
        this.leveys = leveys;
        this.korkeus = korkeus;
        this.mato = new Mato(leveys / 2, korkeus / 2, Suunta.ALAS);
        this.omena = this.uusiOmena();
    }

    public Mato getMato() {
        return this.mato;
    }

    public void setMato(Mato mato) {
        this.mato = mato;
    }

    public Omena getOmena() {
        return this.omena;
    }

    public void setOmena(Omena omena) {
        this.omena = omena;
    }

    public boolean loppu() {
        List<Pala> palat = this.mato.getPalat();
        Pala madonPaa = palat.get(this.mato.getPituus() - 1);

        int x = madonPaa.getX();
        int y = madonPaa.getY();

        if (x < 0 || x >= this.leveys || y < 0 || y >= this.korkeus) {
            return true;
        }

        if (this.mato.osuuItseensa()) {
            return true;
        }

        return false;
    }

    public void paivita() {
        if (this.loppu() == false) {
            this.mato.liiku();

            if (this.mato.osuu(this.omena)) {
                this.setOmena(this.uusiOmena());

                this.mato.kasva();
            }
        }
    }

    public Omena uusiOmena() {
        Random arpoja = new Random();
        Omena omena = null;

        while (true) {
            omena = new Omena(arpoja.nextInt(this.leveys), 
                    arpoja.nextInt(this.korkeus));

            if (this.mato.osuu(omena) == false) {
                break;
            }
        }
        
        return omena;
    }
}
