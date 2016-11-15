/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAORecetteJournaliere;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import transferObject.RecetteJournaliere;

/**
 *
 * @author Himmat
 */
public class DAORecetteJournaliereMySQL implements DAORecetteJournaliere {

    private final static DAORecetteJournaliereMySQL uniqueInstance = new DAORecetteJournaliereMySQL();

    public static DAORecetteJournaliereMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public boolean insertRecette(RecetteJournaliere recette) {
        String requete = "Insert into recetteJournaliere(recette, idEmploye, idCalendrier ) values ("
                + recette.getRecetteTotal() + "," + recette.getIdVendeur() + "," + recette.getIdCalendrier() + ")";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        return ok;
    }

    @Override
    public ArrayList<RecetteJournaliere> selectRecette(int numeroMois, String ordre) {
        ArrayList<RecetteJournaliere> myList = new ArrayList();
        String num = Integer.toString(numeroMois);
        if(numeroMois < 10 ){
            num = "0" + numeroMois;
        }
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String req = "select sum(rec.recette), cal.idCalendrier, cal.dateJour "
                + "from calendrier cal "
                + "join recettejournaliere rec on cal.idCalendrier = rec.idcalendrier "
                + "where strftime('%Y', cal.dateJour) = '"+year+"' and "
                + "strftime('%m', cal.dateJour) = '" + num
                + "' group by cal.idCalendrier"
                + " order by cal.idCalendrier " + ordre;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        System.out.println(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new RecetteJournaliere(resu.getDouble(1), resu.getInt(2), resu.getString(3)));
            }
        } catch (SQLException e) {

            System.out.println("selectRecette : " + e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public ArrayList<RecetteJournaliere> selectRecetteMois(int annee, String ordre) {
        ArrayList<RecetteJournaliere> myList = new ArrayList();

        String req = "select strftime('%m', cal.dateJour), sum(rec.recette) "
                + "from calendrier cal "
                + "join recettejournaliere rec on cal.idCalendrier = rec.idcalendrier "
                + "where strftime('%Y',dateJour) = '" + annee
                + "' group by strftime('%m', cal.dateJour)"
                + " order by sum(rec.recette) " + ordre;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);

        try {
            while (resu.next()) {

                myList.add(new RecetteJournaliere(resu.getInt(1), resu.getDouble(2)));
            }
        } catch (SQLException e) {

            System.out.println("selectRecetteMois : " + e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public int nbVenteMois(int numeroMois) {
        String num = "0" + numeroMois;
        int nbTuble = 0;
        String requete = "SELECT count(*) FROM ventes ven "
                + "join calendrier cal on cal.idCalendrier = ven.idCalendrier "
                + "where strftime('%m', cal.dateJour) ='" + num
                + "'";
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(requete);

        try {
            if (resu.next()) {
                nbTuble = resu.getInt(1);
            }
        } catch (SQLException e) {

            System.out.println("select nbVente du mois : " + e.toString());
            System.exit(-1);
        }
        return nbTuble;
    }
}
