/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOListeRapide;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.ListeRapide;

/**
 *
 * @author Himmat
 */
public class DAOListeRapideMySQL implements DAOListeRapide {

    private static final DAOListeRapideMySQL uniqueInstance = new DAOListeRapideMySQL();

    public static DAOListeRapideMySQL getInstance() {
        return uniqueInstance;
    }
    @Override
    public ArrayList<ListeRapide> selectListeRapide(int idEmploye) {
        ArrayList<ListeRapide> myList = new ArrayList();
        String req = "Select lr.codebarre, pro.libelle, pv.prix, pv.quantite, lr.idEmploye "
                + "from listeProduitRapide lr "
                + "join produit pro on lr.codebarre = pro.codebarre "
                + "join prixdevente pv on pro.codebarre = pv.codebarre "
                + " where lr.idEmploye = " + idEmploye
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new ListeRapide(resu.getString(1),  resu.getString(2),
                        resu.getDouble(3), resu.getInt(4), resu.getInt(5)));
            }
        } catch (SQLException e) {

            System.out.println("selectListeRapide : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public boolean insertListeRapide(ListeRapide listeRapide) {
        String requete = "Insert into listeProduitRapide (idEmploye, codeBarre) values ( "
                + listeRapide.getIdEmploye() + ",'" + listeRapide.getCodeBarre() + "'"
                + ")";
        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        return ok;
    }

    @Override
    public boolean deleteListeRapide(ListeRapide listeRapide) {
        String req = "Delete from ListeproduitRapide where idEmploye = "
                + listeRapide.getIdEmploye() + " and codebarre = '" + listeRapide.getCodeBarre()+ "'";
        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);
        return ok;
    }

    @Override
    public boolean updateListeRapide(ListeRapide listeRapide) {
        String req = "Update listeproduitRapide set idEmploye = " + listeRapide.getIdEmploye()
                + ", codeBarre = '" + listeRapide.getCodeBarre()
                + "' where idEmploye = " + listeRapide.getIdEmploye()
                + " and codebarre = '" + listeRapide.getCodeBarre() + "'";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }
}
