/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import transferObject.PrixDeVente;


/**
 *
 * @author Safi
 */
public interface DAOPrixDeVente {
 
    public boolean insertPrixDeVente(PrixDeVente pro);
    public boolean updateProduit(PrixDeVente prod);
   
}
