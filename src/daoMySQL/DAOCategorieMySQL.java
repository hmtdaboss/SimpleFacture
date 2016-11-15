/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package daoMySQL;

import dao.DAOCategorie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.Categorie;

/**
 *
 * @author Himmat
 */
public class DAOCategorieMySQL implements DAOCategorie{
    
    private static final DAOCategorieMySQL uniqueInstance = new DAOCategorieMySQL();

    public static DAOCategorieMySQL getInstance() {
        return uniqueInstance;
    }
    
    @Override
    public ArrayList<Categorie> selectCategorie() {
        ArrayList<Categorie> myList = new ArrayList();

        String req = "select idCat, libelle, tva"
                + " from Categorie"
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
              
                myList.add(new Categorie(resu.getInt(1),resu.getString(2),resu.getDouble(3)));
            }
        } catch (SQLException e) {

            System.out.println("selectCategorie : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
    
    @Override
    public ArrayList<Categorie> selectCategorieRapid() {
        ArrayList<Categorie> myList = new ArrayList();

        String req = "select catR.idCat, cat.libelle, cat.tva"
                + " from catergorieRapide catR"
                + " join Categorie cat on cat.idCat = catR.idCat"
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
              
                myList.add(new Categorie(resu.getInt(1),resu.getString(2),resu.getDouble(3)));
            }
        } catch (SQLException e) {

            System.out.println("selectCategorie : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
    
    @Override
    public ArrayList<Categorie> searchCategorie(String motCle) {
        ArrayList<Categorie> myList = new ArrayList();

        String req = "select idCat, libelle, tva"
                + " from Categorie"
                + " where libelle like '%" + motCle + "%'"
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Categorie(resu.getInt(1),resu.getString(2),resu.getDouble(3)));
            }
        } catch (SQLException e) {
            System.exit(-1);
        }
        return myList;
    }
 
    @Override
    public boolean insertCategorie(Categorie cat) {            
        boolean ok = ConnexionMySQL.getInstance().actionQuery("Insert into Categorie (libelle, tva ) values ( " 
                + "'" + cat.getLibelle()  + "','" + cat.getTva()
                + "')");
        return ok;
    }
    
    @Override
    public boolean insertCategorieRapid(Categorie cat) {            
        boolean ok = ConnexionMySQL.getInstance().actionQuery("Insert into catergorieRapide (idCat ) values ( " 
                + cat.getIdCat()+ ")");
        return ok;
    }
    
    @Override
    public boolean updateCategorie(Categorie cat) {
        String req = "Update Categorie set libelle = '" + cat.getLibelle()
                + "', tva = " + cat.getTva() + " where idCat = '"
                + cat.getIdCat() + "'";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }
    
    @Override
    public boolean deleteCategorie(int idCat) {
        boolean ok = ConnexionMySQL.getInstance().actionQuery("Delete from Categorie where idCat = '"
                + idCat + "'");

        return ok;
    }
    
    @Override
    public boolean deleteCategorieRap(int idCat) {
        boolean ok = ConnexionMySQL.getInstance().actionQuery("Delete from catergorieRapide where idCat = '"
                + idCat + "'");

        return ok;
    }
    
    @Override
    public int getIdCategorie (String nom)
    {      
        int num=-1;
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery ("Select idCat from Categorie where libelle = '"
        + nom + "'");
        try {
            resu.next();
            num = resu.getInt(1);
        }
        catch (SQLException e){
            System.out.println("getIdCategorie : "+e.toString());
        }
        return num;
    }
    
    @Override
    public int lastId() {
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery("SELECT max(idCat) FROM Categorie");
        int num = -1;
        try {
            resu.next();
            num = resu.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return num;

    }
}
