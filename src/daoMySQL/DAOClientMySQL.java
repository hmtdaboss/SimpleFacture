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

        String req = "select cli.idClient, cli.nomSociete, cli.adresse, cli.codepostal, cli.commune, cli.tel, cli.tva, cli.idmag, cli.mail, mag.nomMagasin"
                + " from Client cli"
                + " join Magasin mag on mag.idMag = cli.idMag"
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new Client(resu.getInt(1), resu.getString(2), resu.getString(3),
                        resu.getInt(4), resu.getString(5),resu.getString(6), resu.getString(7), resu.getInt(8), resu.getString(9), resu.getString(10)));

            }
        } catch (SQLException e) {

            System.out.println("selectEmployer : "+ e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public boolean insertClient(Client client) {
        String requete = "Insert into Client ( nomSociete, adresse, codepostal, commune, tel, tva, idmag, mail ) values ('"
                + client.getNomSociete()+ "','" + client.getAdresse()+ "'," + client.getCodepostal()+ ",'"
                + client.getCommune()+ "','" + client.getTel()+ "', '" + client.getTva()+ "' ," + client.getIdMag()+",'" + client.getMail()+"')";

        System.out.println("insertclient " + requete);
        System.out.println(client.toString());
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
        
        System.out.println("update Client : " + req);

        return ok;
    }

   

}
