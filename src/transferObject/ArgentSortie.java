/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package transferObject;

import java.sql.Date;

/**
 *
 * @author Himmat
 */
public class ArgentSortie {
    private int idTrans;
    private String raison;
    private double sommeDonnee;
    private int idCalendrier;
    private int idEmployer;
    private Date dateJour;
    private String dateString;

    public ArgentSortie(int idTrans, String raison, double sommeDonnee, int idCalendrier, int idEmployer) {
        this.idTrans = idTrans;
        this.raison = raison;
        this.sommeDonnee = sommeDonnee;
        this.idCalendrier = idCalendrier;
        this.idEmployer = idEmployer;
    }

    public ArgentSortie(String dateString ,double sommeDonnee, String raison ) {
        this.raison = raison;
        this.sommeDonnee = sommeDonnee;
        this.dateString = dateString;
    }

    public Date getDateJour() {
        return dateJour;
    }

    public int getIdTrans() {
        return idTrans;
    }

    public String getDateString() {
        return dateString;
    }

    public void setIdTrans(int idTrans) {
        this.idTrans = idTrans;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public double getSommeDonnee() {
        return sommeDonnee;
    }

    public void setSommeDonnee(double sommeDonnee) {
        this.sommeDonnee = sommeDonnee;
    }

    public int getIdCalendrier() {
        return idCalendrier;
    }

    public void setIdCalendrier(int idCalendrier) {
        this.idCalendrier = idCalendrier;
    }

    public int getIdEmployer() {
        return idEmployer;
    }

    public void setIdEmployer(int idEmployer) {
        this.idEmployer = idEmployer;
    }
    
    
    
}
