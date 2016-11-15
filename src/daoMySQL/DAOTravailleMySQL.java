/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOTravaille;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.Travaille;

/**
 *
 * @author Himmat
 */
public class DAOTravailleMySQL implements DAOTravaille {

    private static final DAOTravailleMySQL uniqueInstance = new DAOTravailleMySQL();

    public static DAOTravailleMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public ArrayList<Travaille> selectPrestationEmploye(int idEmploye) {
        ArrayList<Travaille> myList = new ArrayList();
        String req = "select strftime('%Y-%m-%d', cal.dateJour), tra.heuredebut, tra.heureFin, mag.nomMagasin , em.nom "
                + "from travaille tra "
                + "join employe em on tra.idEmploye = em.idEmploye "
                + "join magasin mag on tra.idMag = mag.idMag "
                + "join calendrier cal on tra.idCalendrier = cal.idCalendrier "
                + "where tra.idEmploye =" + idEmploye;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
               
                myList.add(new Travaille(resu.getString(1), resu.getString(2),
                        resu.getString(3), resu.getString(4), resu.getString(5)));
            }
        } catch (SQLException e) {

            System.out.println("selectPrestationEmploye : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public ArrayList<Travaille> selectPrestationEmploye() {
        ArrayList<Travaille> myList = new ArrayList();
        String req = "select strftime('%Y-%m-%d', cal.dateJour), tra.heuredebut, tra.heureFin, mag.nomMagasin , em.nom "
                + "from travaille tra "
                + "join employe em on tra.idEmploye = em.idEmploye "
                + "join magasin mag on tra.idMag = mag.idMag "
                + "join calendrier cal on tra.idCalendrier = cal.idCalendrier ";
        System.out.println(req);
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Travaille(resu.getString(1), resu.getString(2),
                        resu.getString(3), resu.getString(4), resu.getString(5)));
            }
        } catch (SQLException e) {

            System.out.println("selectPrestationEmploye : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
    
    @Override
    public ArrayList<Travaille> selectPrestationEmploye(String idEmp, int numeroMois, int annee) {
        if (idEmp.compareTo("Tous")==0) {
            idEmp = "%";
        }
        String num = "0"+numeroMois;
        ArrayList<Travaille> myList = new ArrayList();
        String req = "select strftime('%Y-%m-%d', cal.dateJour), tra.heuredebut, tra.heureFin, mag.nomMagasin , em.nom "
                + "from travaille tra "
                + "join employe em on tra.idEmploye = em.idEmploye "
                + "join magasin mag on tra.idMag = mag.idMag "
                + "join calendrier cal on tra.idCalendrier = cal.idCalendrier "
                + "where em.nom like '"+ idEmp + "' and strftime('%m',cal.dateJour) like '"+num 
                +"' and strftime('%Y',cal.dateJour) like '"+annee +"'";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        System.out.println(req);
        try {
            while (resu.next()) {
                myList.add(new Travaille(resu.getString(1), resu.getString(2),
                        resu.getString(3), resu.getString(4), resu.getString(5)));
            }
        } catch (SQLException e) {

            System.out.println("selectPrestationEmploye : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
}
