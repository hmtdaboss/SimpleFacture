/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOClient;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.Client;
import transferObject.Employe;
import transferObject.Travaille;

/**
 *
 * @author Himmat
 */
public class DAOClientMySQL implements DAOClient  {

    private final static DAOClientMySQL uniqueInstance = new DAOClientMySQL();

    public static DAOClientMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public ArrayList<Client> selectClient() {
        ArrayList<Client> myList = new ArrayList();

        String req = "select idClient, nomSociete, adresse, codepostal, commune, tel, tva, idmag"
                + " from Client"
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new Client(resu.getInt(1), resu.getString(2), resu.getString(3),
                        resu.getInt(4), resu.getString(5),resu.getString(6), resu.getString(7), resu.getInt(8)));

            }
        } catch (SQLException e) {

            System.out.println("selectEmployer : "+ e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public boolean insertProduit(Client client) {
        String requete = "Insert into Employe ( nomSociete, adresse, codepostal, commune, tel, tva, idmag ) values ('"
                + client.getNomSociete()+ "','" + client.getAdresse()+ "'," + client.getCodepostal()+ ",'"
                + client.getCommune()+ "','" + client.getTel()+ "', '" + client.getTva()+ "' ," + client.getIdMag()+")";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        return ok;
    }

    @Override
    public boolean updateClient(Client client) {
        String req = "Update Client set nomSociete = '" + client.getNomSociete()
                + "', adresse = '" + client.getAdresse()
                + "', codepostal = " + client.getCodepostal()
                + ", commune = '" + client.getCommune()
                + "', tel = '" + client.getTel()
                + "', tva = '" + client.getTva()
                + "', idmag = " + client.getIdMag()
                + " where idClient = " + client.getIdClient();

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }

   

}
