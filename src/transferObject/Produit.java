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
public class Produit {

    private String codebarre;
    private String libelle;
    private int idCat;
    private double prixAchat;
    private String nomCat;
    private double prixVente;
    private String nomMagasin;
    private int quantite;
    private double tva = 0;
    private int qteACommander;
    private int topVente;

    /*Le ticket */
    private double montantTva; 
    private double prixHTVA;
    private double prixTotal;
    
    public Produit(String codebarre, String libelle, double prixVente, 
            double tva, int quantite, String nomCat) {
        this.codebarre = codebarre;
        this.libelle = libelle;
        this.prixVente = prixVente;
        this.tva = tva;
        this.quantite = quantite;
        this.nomCat = nomCat;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public Produit() {
        this.prixAchat = 0;
    }

    /*
     public LesProduits(String codeBarre, String nomProduit, int quantite, double prixTotal, 
            double prixUnitaire, double montantTva, double prixHTVA) {
    */
    public Produit(String codebarre, String libelle, double prixAchat,
            double prixVente, String nomCat, int quantite, String nomMagasin, double tva) {
        this.codebarre = codebarre;
        this.libelle = libelle;
        this.prixAchat = prixAchat;
        this.nomCat = nomCat;
        this.prixVente = prixVente;
        this.nomMagasin = nomMagasin;
        this.quantite = quantite;
        this.tva = tva;
    }
    
     public Produit(String codebarre, String libelle, int quantite, double prixTotal, 
            double prixVente, double montantTva, double prixHTVA) {
        this.codebarre = codebarre;
        this.libelle = libelle;
        this.quantite = quantite;
        this.prixVente = prixVente;
        this.prixTotal = prixTotal;
        
        this.montantTva = montantTva;
        this.prixHTVA = prixHTVA;
        
    }
    

    public Produit(String libelle, int topVente) {
        this.libelle = libelle;
        this.topVente = topVente;
    }

    public Produit(String codeBarre, String libelle, String nomCat, int quantite, int qteACommander) {
        this.libelle = libelle;
        this.nomCat = nomCat;
        this.quantite = quantite;
        this.qteACommander = qteACommander;
        this.codebarre = codeBarre;
    }

    public int getTopVente() {
        return topVente;
    }

    public int getQteACommander() {
        return qteACommander;
    }

    public void setQteACommander(int qteACommander) {
        this.qteACommander = qteACommander;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getNomCat() {
        return nomCat;
    }

    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public String getCodebarre() {
        return codebarre;
    }

    public void setCodebarre(String codebarre) {
        this.codebarre = codebarre;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public double getMontantTva() {
        return montantTva;
    }

    public double getPrixHTVA() {
        return prixHTVA;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

}
