/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOArgentSortie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.ArgentSortie;

/**
 *
 * @author Himmat
 */
public class DAOArgentSortieMySQL implements DAOArgentSortie {

    private static final DAOArgentSortieMySQL uniqueInstance = new DAOArgentSortieMySQL();

    public static DAOArgentSortieMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public boolean insertArgent(ArgentSortie arg) {

        String requete = "Insert into Transac ( raison, somme, idCalendrier, idEmploye ) values ( '"
                + arg.getRaison() + "'," + arg.getSommeDonnee() + ","
                + arg.getIdCalendrier() + "," + arg.getIdEmployer() + ")";
        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);

        return ok;

    }

    @Override
    public ArrayList<ArgentSortie> selectArgentSortie(int idEmp, int idCal) {
        ArrayList<ArgentSortie> myList = new ArrayList();
        /*¨String String codebarre, String libelle, double prixAchat, 
         double prixVente, String nomCat, String nomMagasin) {*/
        String req = "Select strftime('%Y-%m-%d', cal.dateJour), tra.somme, tra.raison "
                + "From transac tra "
                + "join calendrier cal on tra.idCalendrier = cal.idCalendrier "
                + "where tra.idEmploye = " + idEmp + " and tra.idCalendrier = "+idCal;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        System.out.println(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new ArgentSortie(resu.getString(1), resu.getDouble(2), resu.getString(3)));
            }
        } catch (SQLException e) {

            System.out.println("selectArgentSortie " +e.toString());
            
            //System.exit(-1);
        }
        return myList;
    }
    
    

}
