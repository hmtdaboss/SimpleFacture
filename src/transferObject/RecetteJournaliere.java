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
public class RecetteJournaliere {
    private double recetteTotal;
    private int idVendeur = 0;
    private int idCalendrier =0;
    private Date dateJ;
    private int numeroMois;
    private String dateString;

    public RecetteJournaliere(double recetteTotal, int idVendeur, int idCalendrier, Date dateJ) {
        this.recetteTotal = recetteTotal;
        this.idVendeur = idVendeur;
        this.idCalendrier = idCalendrier;
        this.dateJ = dateJ;
    }

    public RecetteJournaliere(double recetteTotal, int idCalendrier, String dateString) {
        this.recetteTotal = recetteTotal;
        this.idCalendrier = idCalendrier;
        this.dateString = dateString;
    }

    public RecetteJournaliere(int numeroMois, double recetteTotal) {
        this.recetteTotal = recetteTotal;
        this.numeroMois = numeroMois;
    }

    public RecetteJournaliere(int idVendeur, int idCalendrier) {
        this.idVendeur = idVendeur;
        this.idCalendrier = idCalendrier;
    }

    public String getDateString() {
        return dateString;
    }
    
    
    public Date getDateJ() {
        return dateJ;
    }

    public int getNumeroMois() {
        return numeroMois;
    }

    public double getRecetteTotal() {
        return recetteTotal;
    }

    public void setRecetteTotal(double recetteTotal) {
        this.recetteTotal = recetteTotal;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public void setIdVendeur(int idVendeur) {
        this.idVendeur = idVendeur;
    }

    public int getIdCalendrier() {
        return idCalendrier;
    }

    public void setIdCalendrier(int idCalendrier) {
        this.idCalendrier = idCalendrier;
    }
    
    
}
