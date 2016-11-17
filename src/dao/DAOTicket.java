/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;

import transferObject.Produit;
import transferObject.TVA;

/**
 *
 * @author Himmat
 */
public interface DAOTicket {

    ArrayList<Produit> selectTicket(int idVente);
    public ArrayList<TVA> selectTVAForTVA(int idVente);
    public boolean deleteNbProduitVendu(String codeBarre, int idVente);
    public boolean deleteVente(int idVente);
    public boolean updateTicketQte(int qte, int idVente, String codeBarre, double prixUnitaire);
}
