/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package transferObject;

/**
 *
 * @author Himmat
 */
public class DifferentModPay {
    private int idVente;
    private int idModePayement;
    private double montant;

    public DifferentModPay(int idVente, int idModePayement, double montant) {
        this.idVente = idVente;
        this.idModePayement = idModePayement;
        this.montant = montant;
    }

    public int getIdVente() {
        return idVente;
    }

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public int getIdModePayement() {
        return idModePayement;
    }

    public void setIdModePayement(int idModePayement) {
        this.idModePayement = idModePayement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
    
    
    
}
