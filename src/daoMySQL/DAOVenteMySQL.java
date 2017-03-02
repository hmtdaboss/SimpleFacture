/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOVente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.FonctionXY;
import transferObject.RecetteJournaliere;
import transferObject.Vente;

/**
 *
 * @author Himmat
 */
public class DAOVenteMySQL implements DAOVente {

    private final static DAOVenteMySQL uniqueInstance = new DAOVenteMySQL();

    public static DAOVenteMySQL getInstance() {
        return uniqueInstance;
    }

    private RecetteJournaliere idEmpNidCal(int idEmp, int idCal) {

        String req = "select idEmploye, idCalendrier "
                + "from recettejournaliere "
                + "where idCalendrier = " + idCal + " and idEmploye= " + idEmp;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        RecetteJournaliere rec = new RecetteJournaliere(0, 0);
        try {
            if (resu.next()) {
                rec.setIdVendeur(resu.getInt(1));
                rec.setIdCalendrier(resu.getInt(2));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return rec;
    }

    private double recetteActuelle(int idEmp, int idCal) {

        double recette = 0.0;
        String req = "select recette "
                + "from recettejournaliere "
                + "where idCalendrier = " + idCal + " and idEmploye= " + idEmp;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            if (resu.next()) {
                recette = resu.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return recette;
    }

    private void triggerInsertVente(Vente vente, boolean inWhichTable) {
        if (!inWhichTable) {
            double recette = recetteActuelle(vente.getIdVendeur(), vente.getIdCalendrier()) + vente.getMontantTotal();
            RecetteJournaliere rec = idEmpNidCal(vente.getIdVendeur(), vente.getIdCalendrier());
            if (rec.getIdCalendrier() == vente.getIdCalendrier() && rec.getIdVendeur() == vente.getIdVendeur()) {
                String req = "UPDATE recettejournaliere"
                        + " SET recette= " + recette + ", idEmploye=" + vente.getIdVendeur()
                        + ", idCalendrier=  " + vente.getIdCalendrier()
                        + " WHERE idCalendrier = " + vente.getIdCalendrier()
                        + " and idEmploye = " + vente.getIdVendeur();
                ConnexionMySQL.getInstance().actionQuery(req);
            } else {

                String req = "INSERT INTO recettejournaliere( recette, idEmploye, idCalendrier) "
                        + "VALUES(" + recette + "," + vente.getIdVendeur() + "," + vente.getIdCalendrier() + ")";

                ConnexionMySQL.getInstance().actionQuery(req);
            }
            System.out.println("rectte  : " + recette);
        }

    }

    @Override
    public boolean insertVente(Vente vente, boolean inWhichTable) {
        
        String tableVente = inWhichTable ? "ventes2" : "ventes";
        

        String requete = "Insert into " + tableVente + "( idEmploye,remiseGenerale,idMag, idCalendrier, heure, montantTotal, idClient, typeDocument)"
                + " values ("
                + vente.getIdVendeur() + "," + vente.getRemiseGen() + "," + vente.getIdMagasin() + ","
                + vente.getIdCalendrier() + ",'"
                + vente.getHeure() + "'," + vente.getMontantTotal() + "," + vente.getIdClient() +",'"+vente.getTypeDoc()+ "')";
        System.out.println(requete);

        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        if (ok) {
            triggerInsertVente(vente, inWhichTable);
        }
        return ok;
    }

    @Override
    public boolean insertNbProdVente(Vente vente, boolean inWhichTable) {

        String tableNbProduitVendu = inWhichTable ? "nbproduitvendu2" : "nbproduitvendu";
        double remise = vente.getPrixVente() * vente.getRemiseArt() / 100;
        /*
      
         */
        String requete = "insert into " + tableNbProduitVendu + "(quantite, prixVente,prixHt,"
                + " montantTva,remiseArticle, codeBarre,idVente)"
                + " values( "
                + vente.getQuantite() + "," + (vente.getPrixTotal() - remise) + ","
                + "round(" + vente.getPrixHT() + ",3), round(" + vente.getMontantTVA() + ",3)," + vente.getRemiseArt() + ",'"
                + vente.getCodeBarre() + "',"
                + vente.getIdVente() + ")";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        if (ok) {
            updateProduit(vente.getCodeBarre(), vente.getQuantite());
        }

        return ok;
    }

    private boolean updateProduit(String codebarre, int qte) {
        String req = "Update prixDeVente set quantite = quantite - " + qte
                + " where codeBarre = '"
                + codebarre + "'";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }

    @Override
    public int lastId() {
        String requete = "SELECT max(IdVente) FROM ventes";
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(requete);
        int num = -1;
        try {
            resu.next();
            num = resu.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return num;

    }

    @Override
    public ArrayList<Vente> selectVenteParHeure(int idCalendrier) {
        ArrayList<Vente> myList = new ArrayList();

        String req = "select hour(heure), sum(montantTotal) "
                + "from ventes where idCalendrier = " + idCalendrier
                + " group by hour(heure)";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new Vente(resu.getString(1), resu.getDouble(2)));
            }
        } catch (SQLException e) {

            System.out.println(e.toString());
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

            System.out.println(e.toString());
            System.exit(-1);
        }
        return total;

    }

    @Override
    public double remiseVente(int idVente) {

        double remise = 0;
        String req = "select remiseGenerale from ventes "
                + "where idVente =" + idVente;

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            if (resu.next()) {
                remise = resu.getDouble(1);
            }
        } catch (SQLException e) {

            System.out.println(e.toString());
            System.exit(-1);
        }
        return remise;

    }

    @Override
    public ArrayList<FonctionXY> selectX(int idCalendrier) {
        ArrayList<FonctionXY> myList = new ArrayList();

        String req = "select cat.idCat, cat.libelle, sum(nb.prixVente), "
                + "sum(nb.prixHt), sum(nb.montantTva), cat.tva "
                + "from categorie cat "
                + "join produit pro on cat.idcat=pro.idcat "
                + "join nbproduitvendu nb on pro.codebarre=nb.codebarre "
                + "join ventes ven on nb.idVente=ven.idVente "
                + "join calendrier cal on ven.idCalendrier=cal.idCalendrier "
                + "where cal.idCalendrier = " + idCalendrier
                + " group by cat.idCat";

        System.out.println(req);
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new FonctionXY(resu.getInt(1), resu.getString(2),
                        resu.getDouble(3), resu.getDouble(4), resu.getDouble(5), resu.getDouble(6)));
            }
        } catch (SQLException e) {

            System.out.println("selectX : " + e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public ArrayList<FonctionXY> selectTotalTVA(int idCalendrier) {
        ArrayList<FonctionXY> myList = new ArrayList();

        String req = "select  cat.tva, sum(nb.prixVente), "
                + "sum(nb.prixHt), sum(nb.montantTva) "
                + "from categorie cat "
                + "join produit pro on cat.idcat=pro.idcat "
                + "join nbproduitvendu nb on pro.codebarre=nb.codebarre "
                + "join ventes ven on nb.idVente=ven.idVente "
                + "join calendrier cal on ven.idCalendrier=cal.idCalendrier "
                + "where cal.idCalendrier = " + idCalendrier
                + " group by cat.tva";

        System.out.println(req);
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new FonctionXY(resu.getDouble(1),
                        resu.getDouble(2), resu.getDouble(3), resu.getDouble(4)));
            }
        } catch (SQLException e) {

            System.out.println("selectX : " + e.toString());
            System.exit(-1);
        }
        return myList;
    }

    @Override
    public ArrayList<Vente> selectTicketCaisse(int idVente) {
        ArrayList<Vente> myList = new ArrayList();
        /*¨String String codebarre, String libelle, double prixAchat, 
         double prixVente, String nomCat, String nomMagasin) {*/
        String req = "Select pro.libelle, (proVendu.prixVente/proVendu.quantite) , proVendu.quantite, "
                + "proVendu.prixVente"
                + " from ventes ven "
                + "join nbproduitvendu proVendu on proVendu.idVente = ven.idVente "
                + "join Produit pro on proVendu.codeBarre = pro.codeBarre "
                + "join prixDeVente pvente on pro.codeBarre = pvente.codeBarre "
                + "where ven.idVente = " + idVente
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                myList.add(new Vente(resu.getString(1), resu.getDouble(2), resu.getInt(3), resu.getDouble(4)));
            }
        } catch (SQLException e) {

            System.out.println("selectTicketCaisse : " + e.toString());
            System.exit(-1);

        }
        System.out.println(req);
        return myList;
    }

}
