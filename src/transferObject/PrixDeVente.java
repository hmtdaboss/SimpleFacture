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
public class PrixDeVente {
    private double prix;
    private int quantite;
    private int idMag;
    private String codeBarre;

    public PrixDeVente(double prix, int quantite, int idMag, String codeBarre) {
        this.prix = prix;
        this.quantite = quantite;
        this.idMag = idMag;
        this.codeBarre = codeBarre;
    }

    public PrixDeVente() {
        this.prix = 0;
        this.quantite = 0;
        this.idMag = 1;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getIdMag() {
        return idMag;
    }

    public void setIdMag(int idMag) {
        this.idMag = idMag;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }
    
}
