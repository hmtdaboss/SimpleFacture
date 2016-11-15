/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transferObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import transferObject.Vente;

/**
 *
 * @author Fawad
 */
public class TicketAttente {

    private final ArrayList<Vente> ticket ;
    
    private final String heure;
    private final String dateJour;
    private double total = 0;
    private int nbArticle = 0;
    private double remiseGen = 0;

    
    public TicketAttente(ArrayList<Vente> ticket, double remiseGen) {
        SimpleDateFormat sdfh = new java.text.SimpleDateFormat("HH:mm");
        SimpleDateFormat sdfd = new SimpleDateFormat("MM/dd/yyyy");
        this.heure = sdfh.format(new Date());
        this.dateJour = sdfd.format(new Date());
        this.ticket = ticket;
        this.remiseGen = remiseGen;
        
        
        calculTotal();
    }
    
    private void calculTotal(){
        for (Vente vente : ticket) {
            this.total += vente.getPrixTotal();
            this.nbArticle += vente.getQuantite();
        }
        this.total -= remiseGen;
    }

    
    public int getNbArticle() {
        return nbArticle;
    }

    public double getRemiseGen() {
        return remiseGen;
    }

    
    public double getTotal() {
        return total;
    }

    public String getHeure() {
        return heure;
    }

    public String getDateJour() {
        return dateJour;
    }

    public ArrayList<Vente> getTicket() {
        
        return ticket;
    }

}
