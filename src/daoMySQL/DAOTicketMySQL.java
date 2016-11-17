/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOTicket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import transferObject.Produit;
import transferObject.TVA;

/**
 *
 * @author Himmat
 */
public class DAOTicketMySQL implements DAOTicket {

    private static final DAOTicketMySQL uniqueInstance = new DAOTicketMySQL();

    public static DAOTicketMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public ArrayList<Produit> selectTicket(int idVente) {
        ArrayList<Produit> myList = new ArrayList();
        /*¨String String codebarre, String libelle, double prixAchat, 
             double prixVente, String nomCat, String nomMagasin) {*/
        String req = "Select pro.codeBarre,pro.libelle, proVendu.quantite, proVendu.prixVente, "
                + "(proVendu.prixVente/proVendu.quantite)"
                + ",proVendu.montantTVA, proVendu.prixHT "
                + " from ventes ven "
                + "join nbproduitvendu proVendu on proVendu.idVente = ven.idVente "
                + "join Produit pro on proVendu.codeBarre = pro.codeBarre "
                + "join prixDeVente pvente on pro.codeBarre = pvente.codeBarre "
                + "where ven.idVente = " + idVente
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //String codeBarre, String nomProduit, int quantite, double prixTotal, double prixUnitaire
                myList.add(new Produit(resu.getString(1), resu.getString(2), resu.getInt(3), resu.getDouble(4),
                        resu.getDouble(5), resu.getDouble(6), resu.getDouble(7)));
            }
        } catch (SQLException e) {

            System.out.println("selectTicket : " + e.toString());
            System.exit(-1);
        }
        System.out.println(req);
        return myList;
    }
    
    @Override
    public ArrayList<TVA> selectTVAForTVA(int idVente) {
        ArrayList<TVA> myList = new ArrayList();
        /*¨String String codebarre, String libelle, double prixAchat, 
             double prixVente, String nomCat, String nomMagasin) {*/
        String req = "select cat.tva, pv.montantTva "
                + "from nbproduitvendu pv "
                + "join produit pro on pro.codebarre = pv.codebarre "
                + "join categorie cat on cat.idCat = pro.idCat "
                + "where pv.idVente = " + idVente + " group by cat.tva";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //String codeBarre, String nomProduit, int quantite, double prixTotal, double prixUnitaire
                myList.add(new TVA(resu.getInt(1), resu.getDouble(2)));
            }
        } catch (SQLException e) {

            System.out.println("selectTVAForTVA : " + e.toString());
            System.exit(-1);
        }
        System.out.println(req);
        return myList;
    }

    @Override
    public boolean deleteNbProduitVendu(String codeBarre, int idVente) {
        boolean ok = ConnexionMySQL.getInstance().actionQuery("Delete from nbProduitVendu where codeBarre = '"
                + codeBarre + "' and idVente = " + idVente);

        return ok;
    }

    @Override
    public boolean deleteVente(int idVente) {
        boolean ok = ConnexionMySQL.getInstance().actionQuery("Delete from ventes where idVente = "
                + idVente);

        return ok;
    }

    @Override
    public boolean updateTicketQte(int qte, int idVente, String codeBarre, double prixUnitaire) {
        String req = "Update nbproduitvendu set quantite = " + qte
                + ", prixVente = " + (qte * prixUnitaire)
                + " where idVente = " + idVente
                + " and codeBarre = '" + codeBarre + "'";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }
}
