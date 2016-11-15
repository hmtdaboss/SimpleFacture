/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package daoMySQL;

import dao.DAODifferentModPay;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.DifferentModPay;
import transferObject.SousTotal;

/**
 *
 * @author Himmat
 */
public class DAODifferentModPayMySQL implements DAODifferentModPay {
    
    private final static DAODifferentModPayMySQL uniqueInstance = new DAODifferentModPayMySQL();

    public static DAODifferentModPayMySQL getInstance() {
        return uniqueInstance;
    }

    
    @Override
    public boolean insertDiffModePay(DifferentModPay pay) {
        String requete = "Insert into differentmodepaye ( idVente, idPaye, montant) values("
                + pay.getIdVente() + "," +pay.getIdModePayement()+","+pay.getMontant()+ ")";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        return ok;
    }
    @Override
    public ArrayList<SousTotal> selectModePayement(int idVente) {
        ArrayList<SousTotal> myList = new ArrayList();

         String req = "Select m.idPaye, m.libelle, dif.montant "
                 + "from differentmodepaye dif "
                 + "join modepayement m on dif.idPaye = m.idPaye "
                 + "where dif.idVente = "+idVente;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                 myList.add(new SousTotal(resu.getInt(1), resu.getString(2), resu.getInt(3)));
            }
        } catch (SQLException e) {

            System.out.println("selectModePayement : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
}
