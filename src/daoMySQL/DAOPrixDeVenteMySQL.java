/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;


import dao.DAOPrixDeVente;
import transferObject.PrixDeVente;

/**
 *
 * @author Safi
 */
public class DAOPrixDeVenteMySQL implements DAOPrixDeVente {

    private static final DAOPrixDeVenteMySQL uniqueInstance = new DAOPrixDeVenteMySQL();

    public static DAOPrixDeVenteMySQL getInstance() {
        return uniqueInstance;
    }

   
   
    
    @Override
    public boolean insertPrixDeVente(PrixDeVente pro) { 
        String requete = "Insert into PrixDeVente ( prix, quantite, idMag, codeBarre ) values ( "
                +pro.getPrix()+","+pro.getQuantite()+","
                +pro.getIdMag()+ ",'"+pro.getCodeBarre()+ "')";
        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        return ok;
    }

   
    
    
    
    @Override
    public boolean updateProduit(PrixDeVente prod) {
        String req = "Update PrixDeVente set prix = " + prod.getPrix()
                + ", quantite = " + prod.getQuantite()
                + ", idMag = " + prod.getIdMag()
                + " where codeBarre = '"
                + prod.getCodeBarre()+ "'";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }
}
