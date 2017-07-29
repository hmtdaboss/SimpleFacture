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

    
    boolean insertNbProdVente(Vente vente, boolean inWhichTable);
    public ArrayList<Vente> selectTicketCaisse(int idVente);
    boolean insertVente(Vente vente, boolean inWhichTable);
    public int lastId();
    public ArrayList<Vente> selectVenteParHeure(int idCalendrier);
    public double remiseVente(int idVente);
    public ArrayList<FonctionXY> selectX(int idCalendrier, boolean bdatabase);
    public ArrayList<FonctionXY> selectTotalTVA(int idCalendrier, boolean bdatabase) ;
    
}
