/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoMySQL;

import dao.DAOEmploye;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import transferObject.Employe;
import transferObject.Travaille;

/**
 *
 * @author Himmat
 */
public class DAOEmployeMySQL implements DAOEmploye {

    private final static DAOEmployeMySQL uniqueInstance = new DAOEmployeMySQL();

    public static DAOEmployeMySQL getInstance() {
        return uniqueInstance;
    }

    @Override
    public ArrayList<Employe> selectEmployer() {
        ArrayList<Employe> myList = new ArrayList();

        String req = "select idEmploye, nom, prenom, idRole"
                + " from Employe"
                + " order by 1";

        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            while (resu.next()) {
                //création de l'objet Chanteur
                myList.add(new Employe(resu.getInt(1), resu.getString(2), resu.getString(3),
                        resu.getString(4)));

            }
        } catch (SQLException e) {

            System.out.println("selectEmployer : "+ e.toString());
            System.exit(-1);
        }
        return myList;
    }

    public int heureDejaAjouter(Travaille tra) {
        int nbTuple = 0;

        String req = "select count(*) "
                + "from calendrier cal "
                + "join travaille tra on cal.idCalendrier = tra.idCalendrier "
                + "where tra.idEmploye =" + tra.getIdEmploye()
                + " and tra.idCalendrier = " + tra.getIdCalendrier();
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            if(resu.next()) {
                nbTuple = resu.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("heureDejaAjouter : "+e.toString());
            System.exit(-1);
        }
        return nbTuple;
    }

    @Override
    public boolean insertHeureDebTravaille(Travaille tra) {
        boolean ok = false;
        if (heureDejaAjouter(tra) == 0) {
            String requete = "insert into travaille values ('"
                + tra.getHeureDeb() + "','" + tra.getHeureFin() + "'," + tra.getIdMag() + ","
                + tra.getIdCalendrier() + "," + tra.getIdEmploye() + ")";
            ok = ConnexionMySQL.getInstance().actionQuery(requete);
        }

        return ok;
    }

    @Override
    public boolean insertHeureFin(String heureFin, int idEmploye, int idCalendrier) {
        String req = "Update travaille set heureFin = '" + heureFin
                + "' where idEmploye = " + idEmploye + " and idCalendrier = " + idCalendrier;

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }

    @Override
    public boolean insertProduit(Employe emp) {
        String requete = "Insert into Employe ( idEmploye, nom, prenom, password, idRole ) values ("
                + emp.getIdEmp() + ",'" + emp.getNom() + "','" + emp.getPrenom() + "','"
                + emp.getPassword() + "'," + emp.getIdRole() + ")";

        boolean ok = ConnexionMySQL.getInstance().actionQuery(requete);
        return ok;
    }

    @Override
    public boolean updateEmployer(Employe emp) {
        String req = "Update Employe set nom = '" + emp.getNom()
                + "', prenom = '" + emp.getPrenom()
                + "', password = '" + emp.getPassword()
                + "', idRole = " + emp.getIdRole()
                + " where idEmploye = " + emp.getIdEmp();

        boolean ok = ConnexionMySQL.getInstance().actionQuery(req);

        return ok;
    }

    @Override
    public Employe getIdLogin(int id, String password) {
        Employe emp = null;

        String req = "Select emp.idEmploye, emp.password, rol.nomRole"
                + " from Employe emp"
                + " join role rol on emp.idRole = rol.idRole"
                + " where emp.idEmploye = " + id + " and emp.password = '" + password + "'";
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);

        try {
            if (resu.next()) {
                emp = new Employe();
                emp.setIdEmp(resu.getInt(1));
                emp.setRole(resu.getString(3));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return emp;
    }

    @Override
    public int adminExiste() {
        int tuple = -1;

        String req = "select count(*) from employe "
                + "where idRole = 1";
        ResultSet resu = ConnexionMySQL.getInstance().selectQuery(req);
        try {
            if (resu.next()) {
                tuple = resu.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return tuple;
    }

}
