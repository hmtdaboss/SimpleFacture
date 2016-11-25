/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.Magasin;
import dao.DAOMagasin;


/**
 *
 * @author Himmat
 */
public class DAOMagasinMySQL implements DAOMagasin {

    private final static DAOMagasinMySQL uniqueInstance = new DAOMagasinMySQL();

    public static DAOMagasinMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public ArrayList<Magasin> selectMagasin() {
        ArrayList<Magasin> myList = new ArrayList();

        String req = "select idMag, nomMagasin, adresse, codepostal, commune, tel, tva, mail"
                + " from magasin"
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new Magasin(resu.getInt(1), resu.getString(2), resu.getString(3),
                        resu.getInt(4), resu.getString(5),resu.getString(6), resu.getString(7), resu.getString(8)));

            }
        } catch (SQLException e) {

            System.out.println("selectMagasin : "+ e.toString());
            System.exit(-1);
        }
        return myList;
    }
    

    @Override
    public boolean updateMagasin(Magasin magasin) {
        String req = "Update Client set nomMagasin = '" + magasin.getNomMagasin()
                + "', adresse = '" + magasin.getAdresse()
                + "', codepostal = " + magasin.getCodepostal()
                + ", commune = '" + magasin.getCommune()
                + "', tel = '" + magasin.getTel()
                + "', tva = '" + magasin.getTva()
                + " where idMag = " + magasin.getIdMag();

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }

    @Override
     public int getIdMagasin (String nom)
    {      
        int num=-1;
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery ("Select idMag from Magasin where nomMagasin = '"
        + nom + "'");
        try {
            resu.next();
            num = resu.getInt(1);
        }
        catch (SQLException e){
            System.out.println("getIdMagasin : "+e.toString());
        }
        return num;
    }
   

}
