/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.Employe;
import transferObject.Travaille;

/**
 *
 * @author Himmat
 */
public interface DAOEmploye {
    
    public boolean insertProduit(Employe emp);
    public Employe getIdLogin (int id, String nom);
    public ArrayList<Employe> selectEmployer();
    public boolean updateEmployer(Employe emp);
    public boolean insertHeureDebTravaille(Travaille tra);
    public boolean insertHeureFin(String heureFin, int idEmploye, int idCalendrier);
    public int adminExiste();
}
