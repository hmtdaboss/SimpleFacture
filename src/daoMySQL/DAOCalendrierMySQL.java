/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOCalendrier;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import transferObject.Calendrier;

/**
 *
 * @author Himmat
 */
public class DAOCalendrierMySQL implements DAOCalendrier {

    private final static DAOCalendrierMySQL uniqueInstance = new DAOCalendrierMySQL();

    public static DAOCalendrierMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public boolean insertCalendrier(int id, Date date) {
        String requete = "Insert into calendrier ( idCalendrier, dateJour) values('"
                + id + "','" +date+ "')";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        return ok;
    }

    @Override
    public Calendrier selectDate() {
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery("SELECT idCalendrier, dateJour FROM calendrier"
                + " ORDER BY idCalendrier DESC LIMIT 1");
        Calendrier cal = new Calendrier();
        try {
            if (resu.next()) {
                cal.setIdCalendrier(resu.getInt(1));
                cal.setDateJour(resu.getString(2));
                
            } 

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return cal;

    }

    @Override
    public int idCalendrier(String date) {
        String requete = "select idcalendrier from Calendrier where dateJour = '" + date +"'";
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(requete);
        System.out.println("------------> "+requete);
        int idCal = -1;
        try {
            resu.next();
            idCal = resu.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return idCal;

    }
}
