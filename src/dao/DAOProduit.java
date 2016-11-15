/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.Produit;


/**
 *
 * @author Safi
 */
public interface DAOProduit {

    
    public Produit uneVenteProduit(String codeBarre);
    public boolean insertProduit(Produit pro);
    public ArrayList<Produit> selectProduit();
    public boolean deleteProduit(String codeBarre);
    public boolean updateProduit(Produit prod);
    public ArrayList<Produit> trie(String motCle);
    public ArrayList<Produit> searchProduit(String motCle);
    public ArrayList<Produit> selectProduitFinStock(int qte, String categorie);
    public ArrayList<Produit> selectTopProduitJour(int idCalendrier, int limit);
    public ArrayList<Produit> selectTopProduitMois(int numeroMois, int limit);
}
