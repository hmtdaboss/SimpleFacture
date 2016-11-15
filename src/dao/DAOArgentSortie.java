/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.ArrayList;
import transferObject.ArgentSortie;

/**
 *
 * @author Himmat
 */
public interface DAOArgentSortie {

    boolean insertArgent(ArgentSortie arg);
    public ArrayList<ArgentSortie> selectArgentSortie(int idEmp, int idCal);
    
}
