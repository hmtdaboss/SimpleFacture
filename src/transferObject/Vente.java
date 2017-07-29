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
public class Vente {
    private int idVente;
    private String codeBarre;
    private String libelleProduit;
    private int idVendeur;
    private int idMagasin;
    private int idCalendrier;
    private int idPayement;
    private double prixVente;
    private int quantite;
    private double prixTotal;
    private double tva ;
    private String heure ;
    private double prixHT;
    private double remiseGen;
    private double remiseArt;
    private double montantTVA;
    private double montantTotal;
    private int idClient;
    private String typeDoc;
    private boolean bdatabase;

    public Vente(int idVente,String codeBarre, String libelleProduit, int idVendeur, 
            int idMagasin, int idCalendrier, int idPayement, 
            double prixVente, double prixTotal, int quantite, double tva, String heure
            , double prixHt, double remiseGen, double remiseArt, double montantTVA, double montantTotal) {
        this.idVente = idVente;
        this.libelleProduit = libelleProduit;
        this.idVendeur = idVendeur;
        this.idMagasin = idMagasin;
        this.idCalendrier = idCalendrier;
        this.idPayement = idPayement;
        this.prixVente = prixVente;
        this.quantite = quantite;
        this.codeBarre = codeBarre;
        this.prixTotal = prixTotal;
        this.tva = tva;
        this.heure = heure;
        this.prixHT = prixHt;
        this.montantTVA = montantTVA;
        this.remiseArt = remiseArt;
        this.remiseGen = remiseGen;
        this.montantTotal = montantTotal;
        
    }
    
    

    public Vente(int idVente, int idVendeur, int idMagasin, String heure, 
            double remiseGen, double montantTotal, int idCalendrier, int idClient, String typeDoc, boolean bdatabase) {
        this.idVente = idVente;
        this.idVendeur = idVendeur;
        this.idMagasin = idMagasin;
        this.heure = heure;
        this.remiseGen = remiseGen;
        this.montantTotal = montantTotal;
        this.idCalendrier = idCalendrier;
        this.idClient = idClient;
        this.typeDoc = typeDoc;
        this.bdatabase = bdatabase;
    }

    public Vente(String libelleProduit, double prixVente, int quantite, double prixTotal) {
        this.libelleProduit = libelleProduit;
        this.prixVente = prixVente;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
       
    }

    
    public Vente(int idVente, String codeBarre, int quantite, double prixTotal, 
            double prixHT, double remiseArt, double montantTVA, double tva, double prixVente, String libelleProduit) {
        this.idVente = idVente;
        this.codeBarre = codeBarre;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
        this.prixHT = prixHT;
        this.remiseArt = remiseArt;
        this.montantTVA = montantTVA;
        this.tva = tva;
        this.prixVente = prixVente;
        this.libelleProduit = libelleProduit;
    }

    public Vente(String heure, double prixVente) {
        this.prixVente = prixVente;
        this.heure = heure;
    }

    
    
    public Vente() {
    }
    
    public String getHeure() {
        return heure;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getPrixHT() {
        return prixHT;
    }

    public void setPrixHT(double prixHT) {
        this.prixHT = prixHT;
    }

    public double getRemiseGen() {
        return remiseGen;
    }

    public void setRemiseGen(double remiseGen) {
        this.remiseGen = remiseGen;
    }

    public double getRemiseArt() {
        return remiseArt;
    }

    public void setRemiseArt(double remiseArt) {
        this.remiseArt = remiseArt;
    }

    public double getMontantTVA() {
        return montantTVA;
    }

    public void setMontantTVA(double montantTVA) {
        this.montantTVA = montantTVA;
    }
    
    public void setHeure(String heure) {
        this.heure = heure;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getCodebarre() {
        return codeBarre;
    }

    public void setCodebarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }
    

    public int getIdVente() {
        return idVente;
    }

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public String getLibelleProduit() {
        return libelleProduit;
    }

    public void setLibelleProduit(String libelleProduit) {
        this.libelleProduit = libelleProduit;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public void setIdVendeur(int idVendeur) {
        this.idVendeur = idVendeur;
    }

    public int getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(int idMagasin) {
        this.idMagasin = idMagasin;
    }

    public int getIdCalendrier() {
        return idCalendrier;
    }

    public void setIdCalendrier(int idCalendrier) {
        this.idCalendrier = idCalendrier;
    }

    public int getIdPayement() {
        return idPayement;
    }

    public void setIdPayement(int idPayement) {
        this.idPayement = idPayement;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public boolean isBdatabase() {
        return bdatabase;
    }

    public void setBdatabase(boolean bdatabase) {
        this.bdatabase = bdatabase;
    }
    
    
}
