/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOProduit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.Produit;

/**
 *
 * @author Safi
 */
public class DAOProduitMySQL implements DAOProduit {

    private static final DAOProduitMySQL uniqueInstance = new DAOProduitMySQL();

    public static DAOProduitMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public Produit uneVenteProduit(String codeBarre) {
        Produit prod = null;
        String requete = "Select pro.codeBarre, pro.libelle, vente.prix,"
                + "cat.tva, vente.quantite, cat.libelle from Produit pro "
                + "join prixDeVente vente on pro.codeBarre = vente.codeBarre "
                + "join Categorie cat on pro.idCat = cat.idCat "
                + "where pro.codeBarre like '" + codeBarre + "'";
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(requete);
        try {

            if (resu.next()) {
                prod = new Produit(resu.getString(1), resu.getString(2), resu.getDouble(3), 
                        resu.getInt(4), resu.getInt(5), resu.getString(6));                                
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return prod;
    }

    @Override
    public ArrayList<Produit> selectProduit() {
        ArrayList<Produit> myList = new ArrayList();
        String req = "Select pro.codeBarre,pro.libelle,pro.prixAchat, pvente.prix, cat.libelle, "
                + "pvente.quantite, mag.nomMagasin , cat.tva from Produit pro "
                + "join prixDeVente pvente on pro.codeBarre = pvente.codeBarre "
                + "join Categorie cat on pro.idCat = cat.idCat "
                + "join Magasin mag on pvente.idMag = mag.idMag "
                + "order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Produit(resu.getString(1), resu.getString(2),
                        resu.getDouble(3), resu.getDouble(4),
                        resu.getString(5), resu.getInt(6), resu.getString(7),
                        resu.getDouble(8)));
            }
        } catch (SQLException e) {

            System.out.println("selectProduit :"+e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public ArrayList<Produit> selectProduitFinStock(int qte, String categorie) {
        ArrayList<Produit> myList = new ArrayList();
        if (categorie.compareTo("Tous") == 0) categorie = "%";
        
        String req = "Select pro.codeBarre,pro.libelle,pro.prixAchat, pvente.prix, cat.libelle, "
                + "pvente.quantite, mag.nomMagasin , cat.tva from Produit pro "
                + "join prixDeVente pvente on pro.codeBarre = pvente.codeBarre "
                + "join Categorie cat on pro.idCat = cat.idCat "
                + "join Magasin mag on pvente.idMag = mag.idMag "
                + "where pvente.quantite <" +qte+ " and cat.libelle like '"+categorie
                + "' order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        
        try {
            while (resu.next()) {
                myList.add(new Produit(resu.getString(1), resu.getString(2),
                        resu.getDouble(3), resu.getDouble(4),
                        resu.getString(5), resu.getInt(6), resu.getString(7),
                        resu.getDouble(8)));
            }
        } catch (SQLException e) {

            System.out.println("selectProduitFinStock : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
    
    @Override
    public ArrayList<Produit> selectTopProduitJour(int idCalendrier, int limit) {
        ArrayList<Produit> myList = new ArrayList();

        String req = "SELECT pro.libelle, count(*) "
                + "from produit pro "
                + "join nbproduitvendu ven on pro.codebarre=ven.codebarre "
                + "join ventes ven2 on ven.idVente=ven2.idVente "
                + "where ven2.idCalendrier =  "+idCalendrier
                + " group by pro.codebarre "
                + "limit "+limit;
        
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Produit(resu.getString(1), resu.getInt(2)));
            }
        } catch (SQLException e) {
            System.out.println("selectTopProduitJour : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
    @Override
    public ArrayList<Produit> selectTopProduitMois(int numeroMois, int limit) {
        ArrayList<Produit> myList = new ArrayList();

        String req = "SELECT pro.libelle, count(*) "
                + "from produit pro "
                + "join nbproduitvendu ven on pro.codebarre=ven.codebarre "
                + "join ventes ven2 on ven.idVente=ven2.idVente "
                + " join calendrier cal on ven2.idCalendrier = cal.idCalendrier "
                + "where strftime('%m', cal.dateJour) = "+numeroMois
                + " group by pro.codebarre "
                + "limit "+limit;
        
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Produit(resu.getString(1), resu.getInt(2)));
            }
        } catch (SQLException e) {
            System.out.println("selectTopProduitMois : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }
    
    @Override
    public ArrayList<Produit> searchProduit(String motCle) {
        
        ArrayList<Produit> myList = new ArrayList();
        String req = "Select pro.codeBarre,pro.libelle,pro.prixAchat, pvente.prix, cat.libelle, "
                + "pvente.quantite, mag.nomMagasin , cat.tva from Produit pro "
                + "join prixDeVente pvente on pro.codeBarre = pvente.codeBarre "
                + "join Categorie cat on pro.idCat = cat.idCat "
                + "join Magasin mag on pvente.idMag = mag.idMag "
                + "where pro.libelle like '%" + motCle + "%'"
                + "or pro.codeBarre like '%" + motCle +"%'"
                + "order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Produit(resu.getString(1), resu.getString(2),
                        resu.getDouble(3), resu.getDouble(4),
                        resu.getString(5), resu.getInt(6), resu.getString(7),
                        resu.getDouble(8)));
            }
        } catch (SQLException e) {

            System.out.println("searchProduit : "+e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public boolean insertProduit(Produit pro) {
        String requete = "Insert into Produit ( codeBarre, libelle, prixAchat, idCat ) values ( '"
                + pro.getCodebarre() + "','" + pro.getLibelle() + "',"
                + pro.getPrixAchat() + "," + pro.getIdCat() + ")";
        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        return ok;
    }

    @Override
    public boolean deleteProduit(String codeBarre) {
        boolean ok1 = ConnexionMySQL.getInstance().actionQuery("Delete from prixdevente where codeBarre = '"
                + codeBarre + "'");

        boolean ok = ConnexionMySQL.getInstance().actionQuery("Delete from Produit where codeBarre = '"
                + codeBarre + "'");

        return ok;
    }

    @Override
    public boolean updateProduit(Produit prod) {
        String req = "Update Produit set libelle = '" + prod.getLibelle()
                + "', prixAchat = " + prod.getPrixAchat()
                + ", idCat = " + prod.getIdCat()
                + " where codeBarre = '"
                + prod.getCodebarre() + "'";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }

    @Override
    public ArrayList<Produit> trie(String motCle) {
        if (motCle.compareTo("Tous") == 0) motCle = "%";
        ArrayList<Produit> myList = new ArrayList();
        String req = "Select pro.codeBarre,pro.libelle,pro.prixAchat, pvente.prix, cat.libelle, "
                + "pvente.quantite, mag.nomMagasin , cat.tva from Produit pro "
                + "join prixDeVente pvente on pro.codeBarre = pvente.codeBarre "
                + "join Categorie cat on pro.idCat = cat.idCat "
                + "join Magasin mag on pvente.idMag = mag.idMag "
                + "where cat.libelle like '"+motCle
                + "' order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Produit(resu.getString(1), resu.getString(2),
                        resu.getDouble(3), resu.getDouble(4),
                        resu.getString(5), resu.getInt(6), resu.getString(7),
                        resu.getDouble(8)));
            }
        } catch (SQLException e) {

            System.out.println("(PRODUIT) trie : "+e.toString());
            System.exit(-1);
        }
        return myList;

    }
}
