/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOTransaction;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.Transaction;

/**
 *
 * @author Himmat
 */
public class DAOTransactionMySQL implements DAOTransaction {

    private static final DAOTransactionMySQL uniqueInstance = new DAOTransactionMySQL();

    public static DAOTransactionMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public ArrayList<Transaction> selectTransaction(int numeroMois) {
        ArrayList<Transaction> myList = new ArrayList();

        String num = "0"+numeroMois;
        String req = "select ven.idVente, cal.dateJour, ven.heure, "
                + "sum(prod.prixVente), ven.idEmploye, mag.nomMagasin, ven.typeDocument, ven.idClient "
                + " from ventes ven "
                + "join magasin mag on ven.idMag = mag.idMag "
                + "join calendrier cal on ven.idCalendrier = cal.idCalendrier "
                + "join nbproduitvendu prod on ven.idVente = prod.idVente "
                + "where strftime('%m',cal.dateJour) = " + num
                + " group by ven.idVente"
                + " order by ven.idVente";
        System.out.println(req);
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Transaction(resu.getInt(1), resu.getDate(2),
                        resu.getString(3),resu.getDouble(4),
                        resu.getInt(5), resu.getString(6), resu.getString(7), resu.getInt(8)));
            }
        } catch (SQLException e) {
            System.out.println("selectTransaction : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public ArrayList<Transaction> selectTransactionID(int idCalendrier) {
        ArrayList<Transaction> myList = new ArrayList();
        
        String req = "select ven.idVente, strftime('%Y-%m-%d', cal.dateJour), ven.heure, "
                + "sum(prod.prixVente), ven.idEmploye, mag.nomMagasin, ven.remiseGenerale,  ven.typeDocument, ven.idClient "
                + " from ventes ven "
                + "join magasin mag on ven.idMag = mag.idMag "
                + "join calendrier cal on ven.idCalendrier = cal.idCalendrier "
                + "join nbproduitvendu prod on ven.idVente = prod.idVente "
                + "where ven.idCalendrier = "+idCalendrier
                + " group by ven.idVente"
                + " order by ven.idVente";
        
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        System.out.println(req);
        try {
            while (resu.next()) {
                myList.add(new Transaction(resu.getInt(1), resu.getString(2),
                        resu.getString(3),resu.getDouble(4)-resu.getDouble(7),
                        resu.getInt(5), resu.getString(6), resu.getString(8), resu.getInt(9)));
            }
        } catch (SQLException e) {
            System.out.println("selectTransactionID : "+e.toString());
            //System.exit(-1);
        }
        return myList;
    }
    @Override
    public ArrayList<Transaction> selectTransactionIDEmploye(int idEmploye) {
        ArrayList<Transaction> myList = new ArrayList();

        String req = "select ven.idVente, strftime('%Y-%m-%d', cal.dateJour), ven.heure, "
                + "sum(prod.prixVente), ven.idEmploye, mag.nomMagasin,  ven.typeDocument, ven.idClient "
                + " from ventes ven "
                + "join magasin mag on ven.idMag = mag.idMag "
                + "join calendrier cal on ven.idCalendrier = cal.idCalendrier "
                + "join nbproduitvendu prod on ven.idVente = prod.idVente "
                + "where ven.idEmploye = "+idEmploye
                + " group by ven.idVente"
                + " order by ven.idVente";
        
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Transaction(resu.getInt(1), resu.getString(2),
                        resu.getString(3),resu.getDouble(4),
                        resu.getInt(5), resu.getString(6), resu.getString(7), resu.getInt(8)));
            }
        } catch (SQLException e) {
            System.out.println("selectTransactionIDEmploye : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
    
    @Override
    public ArrayList<Transaction> selectTransactionParDate(Date date1, Date date2) {
        ArrayList<Transaction> myList = new ArrayList();

        String req = "select ven.idVente, strftime('%Y-%m-%d', cal.dateJour), ven.heure, "
                + "sum(prod.prixVente), ven.idEmploye, mag.nomMagasin ,  ven.typeDocument, ven.idClient"
                + " from ventes ven "
                + "join magasin mag on ven.idMag = mag.idMag "
                + "join calendrier cal on ven.idCalendrier = cal.idCalendrier "
                + "join nbproduitvendu prod on ven.idVente = prod.idVente "
                + "where cal.dateJour between '" + date1 + "' and '" + date2
                + "' group by ven.idVente"
                + " order by ven.idVente";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                 myList.add(new Transaction(resu.getInt(1), resu.getString(2),
                         resu.getString(3),resu.getDouble(4),
                        resu.getInt(5), resu.getString(6), resu.getString(7), resu.getInt(8)));
            }
        } catch (SQLException e) {

            System.out.println("selectTransactionParDate : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
    @Override
    public ArrayList<Transaction> rechercherTrans(String motCle) {
        ArrayList<Transaction> myList = new ArrayList();

         String req = "select ven.idVente, strftime('%Y-%m-%d', cal.dateJour), ven.heure, "
                 + "sum(prod.prixVente), ven.idEmploye, mag.nomMagasin ,  ven.typeDocument, ven.idClient"
                + " from ventes ven "
                + "join magasin mag on ven.idMag = mag.idMag "
                + "join calendrier cal on ven.idCalendrier = cal.idCalendrier "
                + "join nbproduitvendu prod on ven.idVente = prod.idVente"
                + " where ven.idVente like '%" + motCle + "%'"
                + " group by ven.idVente"
                + " order by ven.idVente";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                 myList.add(new Transaction(resu.getInt(1), resu.getString(2),
                         resu.getString(3),resu.getDouble(4),
                        resu.getInt(5), resu.getString(6), resu.getString(7), resu.getInt(8)));
            }
        } catch (SQLException e) {

            System.out.println("rechercherTrans : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }

    /**
     *
     * @param idCal
     * @param idVend
     * @return
     */
    @Override
    public double selectTotal(int idCal, int idVend) {
        double total = 0.0;
        String req = "Select sum(nbPro.prixVente) "
                + "from nbproduitvendu nbPro "
                + "join ventes ven on nbPro.idVente = ven.idVente "
                + "where ven.idCalendrier =  " + idCal + " and ven.idEmploye = " + idVend;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            if (resu.next()) {
                total = resu.getDouble(1);

            }
        } catch (SQLException e) {

            System.out.println("selectTotal : "+e.toString());
            System.exit(-1);
        }
        return total;

    }
    
    @Override
    public ArrayList<Transaction> selectTotalEmp(int idEmploye, int idCalendrier) {
        ArrayList<Transaction> myList = new ArrayList();
        String req = "select mode.libelle, sum(df.montant), strftime('%Y-%m-%d', cal.dateJour) "
                + "from modepayement mode "
                + "join differentmodepaye df on mode.idPaye = df.idPaye "
                + "join ventes ven on df.idVente = ven.idVente "
                + "join calendrier cal on ven.idCalendrier = cal.idCalendrier "
                + "where idEmploye =" +idEmploye+ " and ven.idCalendrier = " +idCalendrier
                + " group by df.idPaye";

        System.out.println(req);
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Transaction(resu.getString(1), resu.getDouble(2),resu.getString(3)));
            }
        } catch (SQLException e) {

            System.out.println("selectTotalEmp : "+e.toString());
            //System.exit(-1);
        }
        return myList;

    }

    @Override
    public ArrayList<Transaction> selectTotal(int idCalendrier) {
        ArrayList<Transaction> myList = new ArrayList();
        String req = "select mode.libelle, sum(df.montant), strftime('%Y-%m-%d', cal.dateJour) "
                + "from modepayement mode "
                + "join differentmodepaye df on mode.idPaye = df.idPaye "
                + "join ventes ven on df.idVente = ven.idVente "
                + "join calendrier cal on ven.idCalendrier = cal.idCalendrier "
                + "where ven.idCalendrier = " +idCalendrier
                + " group by df.idPaye";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Transaction(resu.getString(1), resu.getDouble(2),resu.getString(3)));
            }
        } catch (SQLException e) {

            System.out.println("SelectTotal : "+e.toString());
            System.exit(-1);
        }
        return myList;

    }
    

}
