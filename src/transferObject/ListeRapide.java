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
public class ListeRapide {
    private int idEmploye;
    private String codeBarre;
    private double prix;
    private String nomProduit;
    private int quantite;

    public ListeRapide(int idEmploye, String codeBarre) {
        this.idEmploye = idEmploye;
        this.codeBarre = codeBarre;
    }

    public ListeRapide(String codeBarre,String nomProduit, double prix,  int quantite,int idEmploye) {
        this.codeBarre = codeBarre;
        this.prix = prix;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.idEmploye = idEmploye;
    }

    public double getPrix() {
        return prix;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }
    
}
