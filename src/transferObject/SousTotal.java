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
public class SousTotal {
    private int idPayement;
    private String nomMode;
    private double montant;
     

    public SousTotal(int idPayement, String nomMode, double montant) {
        this.idPayement = idPayement;
        this.nomMode = nomMode;
        this.montant = montant;
        
    }

    public int getIdPayement() {
        return idPayement;
    }

    public void setIdPayement(int idPayement) {
        this.idPayement = idPayement;
    }

    public String getNomMode() {
        return nomMode;
    }

    public void setNomMode(String nomMode) {
        this.nomMode = nomMode;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
    
}
