/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.sql.Date;
import java.util.ArrayList;
import transferObject.Transaction;

/**
 *
 * @author Himmat
 */
public interface DAOTransaction {

    ArrayList<Transaction> selectTransaction(int numeroMois);
    public ArrayList<Transaction> selectTransactionID(int idCalendrier);
    public double selectTotal(int idCal, int idVend);
    public ArrayList<Transaction> selectTotalEmp(int idEmploye, int idCalendrier);
    public ArrayList<Transaction> selectTotal(int idCalendrier);
    public ArrayList<Transaction> rechercherTrans(String motCle);
    public ArrayList<Transaction> selectTransactionParDate(Date date1, Date date2);
    public ArrayList<Transaction> selectTransactionIDEmploye(int idEmploye);
    
}
