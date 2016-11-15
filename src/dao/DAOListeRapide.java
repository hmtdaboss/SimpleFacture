/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.ListeRapide;

/**
 *
 * @author Himmat
 */
public interface DAOListeRapide {

    boolean deleteListeRapide(ListeRapide listeRapide);

    boolean insertListeRapide(ListeRapide listeRapide);

    ArrayList<ListeRapide> selectListeRapide(int idEmploye);

    boolean updateListeRapide(ListeRapide listeRapide);
    
}
