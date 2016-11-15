/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticket;

import java.util.ArrayList;
import transferObject.FonctionXY;
import transferObject.SousTotal;
import transferObject.Transaction;
import transferObject.Vente;

/**
 *
 * @author Fawad
 */
public class TicketInfo implements Cloneable {

    int idTicket;
    int nbArticle;
    double sommeTotal;
    double sommeRendu;
    ArrayList<Vente> listeProduit;
    int idVendeur;
    private ArrayList<SousTotal> listeST;
    ArrayList<FonctionXY> listXY;
    ArrayList<Transaction> listTotGen;

    public TicketInfo(int idTicket, int nbArticle, double sommeTotal,
            double sommeRendu, ArrayList<Vente> listeProduit, int inVendeur, ArrayList<SousTotal> listeST) {
        this.idTicket = idTicket;
        this.nbArticle = nbArticle;
        this.sommeTotal = sommeTotal;
        this.sommeRendu = sommeRendu;
        this.idVendeur = inVendeur;
        this.listeProduit = listeProduit;
        this.listeST = listeST;

    }

    public TicketInfo(ArrayList<FonctionXY> listXY){
        this.listXY = listXY;
    }

    public TicketInfo(ArrayList<Transaction> listTotGen, int a) {
        
        this.listTotGen = listTotGen;
        
    }
    
    
    
    @Override
    public TicketInfo clone() {
        TicketInfo ticket = null;
        try {
			// On récupère l'instance à renvoyer par l'appel de la 
            // méthode super.clone()
            ticket =(TicketInfo) super.clone();
        } catch (CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implémentons 
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return ticket;
    }

    public ArrayList<SousTotal> getListeST() {
        return listeST;
    }

    public ArrayList<FonctionXY> getListXY() {
        return listXY;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public int getNbArticle() {
        return nbArticle;
    }

    public double getSommeTotal() {
        return sommeTotal;
    }

    public double getSommeRendu() {
        return sommeRendu;
    }

    public ArrayList<Vente> getListeProduit() {
        return listeProduit;
    }

    public ArrayList<Transaction> getListTotGen() {
        return listTotGen;
    }
    

}
