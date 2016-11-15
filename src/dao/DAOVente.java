/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.FonctionXY;
import transferObject.Vente;

/**
 *
 * @author Himmat
 */
public interface DAOVente {

    
    boolean insertNbProdVente(Vente vente);
    boolean insertNbProdVente2(Vente vente);
    public ArrayList<Vente> selectTicketCaisse(int idVente);
    boolean insertVente(Vente vente);
    public boolean insertVente2(Vente vente);
    public int lastId();
    public ArrayList<Vente> selectVenteParHeure(int idCalendrier);
    public double remiseVente(int idVente);
    public ArrayList<FonctionXY> selectX(int idCalendrier);
    public ArrayList<FonctionXY> selectTotalTVA(int idCalendrier) ;
    
}
