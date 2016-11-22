/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

/**
 *
 * @author Safi
 */
public class Factory {

    /*Objet dao pour les produit */
    public static dao.DAOProduit getDAOProduit() {
        return daoMySQL.DAOProduitMySQL.getInstance();
    }
    /*dao methode utile : les méthode régulièrement utilisé dans
    qasi toutes les classe seront mis ici */
    public static Utile.MethodeUtile getMethodeUtile() {
        return Utile.MethodeUtile.getInstance();
    }

    public static dao.DAOCategorie getDAOCategorie() {
        return daoMySQL.DAOCategorieMySQL.getInstance();
    }
    /*Les ventes effectué */
    public static dao.DAOVente getDAOVente() {
        return daoMySQL.DAOVenteMySQL.getInstance();
    }

    public static dao.DAOCalendrier getCalendrier() {
        return daoMySQL.DAOCalendrierMySQL.getInstance();
    }

    public static dao.DAODifferentModPay getDifferentModPay() {
        return daoMySQL.DAODifferentModPayMySQL.getInstance();
    }

    public static dao.DAOTravaille getTravaille() {
        return daoMySQL.DAOTravailleMySQL.getInstance();
    }

    public static dao.DAOEmploye getDAOEmploye() {
        return daoMySQL.DAOEmployeMySQL.getInstance();
    }

    public static dao.DAOListeRapide getListeRapide() {
        return daoMySQL.DAOListeRapideMySQL.getInstance();
    }
    
    public static dao.DAOPrixDeVente getDAOPrixDeVente() {
        return daoMySQL.DAOPrixDeVenteMySQL.getInstance();
    }
    
    public static dao.DAOTransaction getTransaction() {
        return daoMySQL.DAOTransactionMySQL.getInstance();
    }
    
    public static dao.DAORecetteJournaliere getRecetteJournaliere(){
        return daoMySQL.DAORecetteJournaliereMySQL.getInstance();
    }
    
    public static dao.DAOArgentSortie getArgentSortie(){
        return daoMySQL.DAOArgentSortieMySQL.getInstance();
    }
    
    public static dao.DAOTicket getTicket(){
        return daoMySQL.DAOTicketMySQL.getInstance();
    } 
    
    public static dao.DAOClient getClient(){
        return daoMySQL.DAOClientMySQL.getInstance();
    }
}
