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
public class FonctionXY {
    private  int idCat = 0;
    private  String libelle;
    private final double montantTotal;
    private final double montantHT;
    private final double prixTva;
    private final double catTva;

    public FonctionXY(int idCat, String libelle, double montantTotal, 
            double montantHT, double prixTva, double catTva) {
        this.idCat = idCat;
        this.libelle = libelle;
        this.montantTotal = montantTotal;
        this.montantHT = montantHT;
        this.prixTva = prixTva;
        this.catTva = catTva;
    }

    public FonctionXY(double catTva, double montantTotal, double montantHT, double prixTva) {
        this.montantTotal = montantTotal;
        this.montantHT = montantHT;
        this.prixTva = prixTva;
        this.catTva = catTva;
    }

    public int getIdCat() {
        return idCat;
    }

    public String getLibelle() {
        return libelle;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public double getMontantHT() {
        return montantHT;
    }

    public double getPrixTva() {
        return prixTva;
    }

    public double getCatTva() {
        return catTva;
    }
    
    
    
}
