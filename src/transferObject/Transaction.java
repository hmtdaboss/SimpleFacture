/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package transferObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Himmat
 */
public class Transaction {
    private int idVente;
    private Date dtVente;
    private double total;
    private int idVendeur;
    private String nomMagasin;
    private String heure;
    private double montant;
    private Date dateJour;
    private String libellePayement;
    private String dateString;
    private String typeDoc;
    private int idClient;

    public Transaction(int idVente, Date dtVente,String heure, double total, 
            int idVendeur, String nomMagasin, String typeDoc, int idClient) {
        this.idVente = idVente;
        this.dtVente = dtVente;
        this.total = total;
        this.idVendeur = idVendeur;
        this.nomMagasin = nomMagasin;
        this.heure = heure;
        this.typeDoc = typeDoc;
        this.idClient = idClient;
    }
    
    public Transaction(int idVente, Date dtVente,String heure, double total, 
            int idVendeur, String nomMagasin) {
        this.idVente = idVente;
        this.dtVente = dtVente;
        this.total = total;
        this.idVendeur = idVendeur;
        this.nomMagasin = nomMagasin;
        this.heure = heure;
        
    }
    
    public Transaction(int idVente, String dateString, String heure, double total, 
            int idVendeur, String nomMagasin, String typeDoc, int idClient) {
        this.idVente = idVente;
        this.dateString = dateString;
        this.total = total;
        this.idVendeur = idVendeur;
        this.nomMagasin = nomMagasin;
        this.heure = heure;
        this.typeDoc = typeDoc;
        this.idClient = idClient;
    }

    public Transaction(String libellePayement , double montant, String dateString) {
        this.montant = montant;
        this.dateString = dateString;
        this.libellePayement = libellePayement;
    }

    public String getDateString() {
        return dateString;
    }

    public double getMontant() {
        return montant;
    }

    public Date getDateJour() {
        return dateJour;
    }

    public String getLibellePayement() {
        return libellePayement;
    }
    
    
    public String getDateTransactionSQL() {
        String tmp;

        if (this.dtVente == null)
          tmp = "";
        else
          {
          SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
          tmp = dateParser.format(this.dtVente);
          }
        return tmp;
    }
    
    public int getIdVente() {
        return idVente;
    }

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public Date getDtVente() {
        return dtVente;
    }

    public void setDtVente(Date dtVente) {
        this.dtVente = dtVente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public void setIdVendeur(int idVendeur) {
        this.idVendeur = idVendeur;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }
    
}
