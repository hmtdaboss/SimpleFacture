/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.Travaille;

/**
 *
 * @author Himmat
 */
public interface DAOTravaille {

    public ArrayList<Travaille> selectPrestationEmploye();
    public ArrayList<Travaille> selectPrestationEmploye(int idEmploye);
    public ArrayList<Travaille> selectPrestationEmploye(String idEmp, int numeroMois, int annee);
    
}
